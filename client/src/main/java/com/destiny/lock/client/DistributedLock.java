package com.destiny.lock.client;

import com.destiny.lock.api.LockRequest;
import com.destiny.lock.api.base.LockException;
import com.destiny.lock.api.base.LockResponseCode;
import com.destiny.lock.client.rest.LockRestClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class DistributedLock {
    private static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);
    @Autowired
    private LockRestClient lockRestClient;
    /**
     * 获取锁的超时时间
     */
    private static long DEFAULT_APPLY_TIMEOUT = 2000;
    private long applyIntervalTime = 100;

    /**
     * 加锁
     *
     * @param lockCode 锁的key
     * @param expiredTime 持有锁的过期时间
     */
    public void lock(String lockCode, long expiredTime) throws LockException {
        lock(lockCode, expiredTime, DEFAULT_APPLY_TIMEOUT);
    }

    /**
     * @param lockCode 锁的key
     * @param expiredTime 持有锁的过期时间
     * @param applyTimeout 锁申请超时时间
     *
     * @throws LockException
     */
    public void lock(String lockCode, long expiredTime, long applyTimeout) throws LockException {
        if (StringUtils.isBlank(lockCode) || expiredTime <= 0) {
            throw new LockException(LockResponseCode.ILLEGAL_REQUEST_PARAM);
        }
        // 锁申请超时时间
        long beforeApplyTime = System.currentTimeMillis();
        // 循环获取锁，直到获取锁或者达到锁申请超时时间
        LockException exception = null;
        do {
            try {
                exception = null;
                doLock(lockCode, expiredTime);
                break;
            } catch (Exception e) {
                logger.info("加锁{}失败", lockCode);
                if (e instanceof LockException) {
                    exception = (LockException) e;
                } else {
                    exception = new LockException(LockResponseCode.UNKNOW_ERROR);
                }
                logger.info("正在尝试重新加锁{}", lockCode);
                try {
                    TimeUnit.MILLISECONDS.sleep(applyIntervalTime);
                } catch (Exception e1) {
                }
            }
        } while ((System.currentTimeMillis() - beforeApplyTime) <= applyTimeout);

        // 若有异常, 表示没申请到锁
        if (exception != null) {
            logger.info("最终加锁{}失败", lockCode);
            throw exception;
        }
        logger.info("加锁{}成功", lockCode);
        if (DistributedLockSnapshot.ALL_LOCKS.get(lockCode) != null
                && DistributedLockSnapshot.ALL_LOCKS.get(lockCode).getRight() != null) {
            DistributedLockSnapshot.ALL_LOCKS.get(lockCode).getRight().setLocked(true);
        } else {
            logger.error("设置加锁成功时, ALL_LOCKS快照中没有锁" + lockCode);
            throw new LockException(LockResponseCode.UNKNOW_ERROR);
        }
        // 设置当前线程拥有的锁(单线程,不需要加锁)
        Set<String> currentOwnedLocks = DistributedLockSnapshot.CURRENT_THREAD_OWNED_LOCKS.get();
        if (currentOwnedLocks == null) {
            DistributedLockSnapshot.CURRENT_THREAD_OWNED_LOCKS.set(new HashSet<String>());
        }
        DistributedLockSnapshot.CURRENT_THREAD_OWNED_LOCKS.get().add(lockCode);
    }

    private void doLock(String lockCode, long expiredTime) throws Exception {
        LockRequest lockRequest = createLockRequest(lockCode, expiredTime);
        MutablePair<Thread, LockRequest> existLockRequest = DistributedLockSnapshot.ALL_LOCKS.putIfAbsent(lockCode, new
                MutablePair<Thread, LockRequest>(Thread.currentThread(), lockRequest));

        // 返回加锁失败
        if (existLockRequest != null) {
            throw new LockException(LockResponseCode.LOCK_ERROR);
        }
        try {
            // 远程调用
            lockRestClient.lock(lockRequest);
        } catch (Exception e) {
            DistributedLockSnapshot.ALL_LOCKS.remove(lockCode);
            throw e;
        }
    }

    /**
     * 解锁
     */
    public boolean unlock(String lockCode) {
        if (StringUtils.isBlank(lockCode)) {
            throw new LockException(LockResponseCode.ILLEGAL_REQUEST_PARAM);
        }
        // 从拥有的锁列表中移除,一定能执行成功
        if (!removeOwnedLock(lockCode)) {
            logger.warn("解锁{}失败,锁不存在", lockCode);
            return false;
        }
        MutablePair<Thread, LockRequest> unlockLockPair = DistributedLockSnapshot.ALL_LOCKS.remove(lockCode);
        if (unlockLockPair == null || unlockLockPair.getRight() == null
                || StringUtils.isBlank(unlockLockPair.getRight().getLockCode())) {
            logger.warn("解锁{}失败,锁不存在", lockCode);
            return false;
        }
        // 远程调用
        try {
            lockRestClient.unlock(unlockLockPair.getRight().getApplicant(), unlockLockPair.getRight().getLockCode(),
                    unlockLockPair.getRight().getRequestId());
        } catch (Exception e) {
            logger.error("解锁{}失败", lockCode, e);
            return false;
        }
        return true;
    }

    private boolean removeOwnedLock(String lockCode) {
        Set<String> currentOwnedLocks = DistributedLockSnapshot.CURRENT_THREAD_OWNED_LOCKS.get();
        boolean hasLock = false;
        if (currentOwnedLocks != null) {
            hasLock = currentOwnedLocks.remove(lockCode);
        }
        if (CollectionUtils.isEmpty(currentOwnedLocks)) {
            DistributedLockSnapshot.CURRENT_THREAD_OWNED_LOCKS.remove();
        }
        return hasLock;
    }

    private LockRequest createLockRequest(String lockCode, long expiredTime) {
        LockRequest lockRequest = new LockRequest();
        lockRequest.setLockCode(lockCode);
        lockRequest.setRequestId(UUID.randomUUID().toString());
        lockRequest.setExpiredTime(expiredTime);

        Set<String> currentOwnedLocks = DistributedLockSnapshot.CURRENT_THREAD_OWNED_LOCKS.get();
        lockRequest.setOwnedLocks(currentOwnedLocks == null ? "" : (StringUtils.join(currentOwnedLocks, ",")));
        return lockRequest;
    }

    public void setApplyIntervalTime(long applyIntervalTime) {
        this.applyIntervalTime = applyIntervalTime;
    }
}

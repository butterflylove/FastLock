package com.destiny.lock.client;

import com.destiny.lock.api.LockRequest;
import com.destiny.lock.api.base.LockException;
import com.destiny.lock.api.base.LockResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DistributedLock {
    private static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);
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
                // TODO
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
    }

    private void doLock(String lockCode, long expiredTime) throws Exception {
        LockRequest lockRequest = createLockRequest(lockCode, expiredTime);
        MutablePair<Thread, LockRequest> existLockRequest = DistributedLockSnapshot.ALL_LOCKS.putIfAbsent(lockCode, new
                MutablePair<>(Thread.currentThread(), lockRequest));

        // 返回加锁失败
        if (existLockRequest != null) {
            throw new LockException(LockResponseCode.LOCK_ERROR);
        }
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
}

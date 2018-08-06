package com.destiny.lock.client.rest;

import com.destiny.lock.api.BaseResponse;
import com.destiny.lock.api.LockRequest;
import com.destiny.lock.api.LockTouchRequest;
import com.destiny.lock.api.UnlockModel;
import com.destiny.lock.api.base.LockException;
import com.destiny.lock.api.base.LockResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

public class LockRestClient extends AbstractRestClient {
    private static final Logger logger = LoggerFactory.getLogger(LockRestClient.class);
    private static final String HOST = "http://127.0.0.1:8080";

    public LockRestClient() {
        init();
    }

    /**
     * 加锁
     */
    public void lock(LockRequest lockRequest) {
        Assert.notNull(lockRequest, "加锁的对象为空");
        BaseResponse result = null;
        try {
            result = postForObject(HOST + "/lock", lockRequest, BaseResponse.class);
        } catch (Exception e) {
            logger.warn("加锁{}是异常:{}", lockRequest, e);
            throw new LockException(LockResponseCode.NET_CALL_EXCEPTION);
        }
        if (result != null && LockResponseCode.SUCCESS.getErrorCode().equals(result.getResult())) {
            logger.info("加锁成功{}", lockRequest);
        } else {
            throw new LockException(LockResponseCode.LOCK_ERROR);
        }
    }

    /**
     * 解锁
     */
    public void unlock(String applicant, String lockCode, String requestId) {
        Assert.hasText(lockCode, "解锁的锁编号为空");
        BaseResponse result = null;
        try {
            UnlockModel unlockModel = new UnlockModel(applicant, lockCode, requestId);
            result = postForObject(HOST + "/unlock", unlockModel, BaseResponse.class);
        } catch (Exception e) {
            logger.warn("解锁{}-{}-{}时异常:{}", applicant, lockCode, requestId, e);
            throw new LockException(LockResponseCode.NET_CALL_EXCEPTION);
        }

        if (result != null && LockResponseCode.SUCCESS.getErrorCode().equals(result.getResult())) {
            logger.info("解锁成功{}-{}-{}", applicant, lockCode, requestId);
        } else {
            throw new LockException(LockResponseCode.UNLOCK_ERROR);
        }
    }

    /**
     * touch, 延迟锁过期时间
     */
    public void touch(LockTouchRequest lockTouchRequest) {
        if (lockTouchRequest == null || CollectionUtils.isEmpty(lockTouchRequest.getLocks())) {
            return;
        }
        BaseResponse result = null;
        try {
            result = postForObject(HOST + "/touch", lockTouchRequest, BaseResponse.class);
        } catch (Exception e) {
            logger.warn("touch{}时异常:{}", lockTouchRequest, e);
            throw new LockException(LockResponseCode.NET_CALL_EXCEPTION);
        }

        if (result != null && LockResponseCode.SUCCESS.getErrorCode().equals(result.getResult())) {
            logger.info("touch成功{}", lockTouchRequest);
        } else {
            throw new LockException(LockResponseCode.TOUCH_ERROR);
        }
    }
}

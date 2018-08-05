package com.destiny.lock.service.controller;

import com.destiny.lock.api.*;
import com.destiny.lock.api.base.LockException;
import com.destiny.lock.api.base.LockResponseCode;
import com.destiny.lock.service.biz.LockService;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by zhangtianlong on 18/7/28.
 */
@RestController
public class LockController {
    private static final Logger logger = LoggerFactory.getLogger(LockController.class);
    @Autowired
    private LockService lockService;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String test(@RequestParam(name = "name") String name) {
        Jedis jedis = jedisPool.getResource();
        return jedis.get(name);
    }

    /**
     * 加锁
     */
    @RequestMapping(value = "/lock", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse lock(@RequestBody LockRequest lockRequest) {
        try {
            lockService.lock(lockRequest.getLockCode(), lockRequest.getRequestId(), lockRequest.getExpiredTime());
        } catch (Exception e) {
            logger.warn(String.format("redis加锁失败: %s", lockRequest), e);
            throw new LockException(LockResponseCode.LOCK_ERROR);
        }
        return new BaseResponse();
    }

    /**
     * 解锁
     */
    @RequestMapping(value = "/unlock/{applicant}/{lockCode}/{requestId}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse unlock(@PathVariable("applicant") String applicant, @PathVariable("requestId") String requestId,
                               @PathVariable("lockCode") @NotEmpty @NotNull String lockCode) {
        try {
            lockService.unlock(lockCode, requestId);
        } catch (Exception e) {
            logger.warn(String.format("redis解锁失败: %s", lockCode), e);
            throw new LockException(LockResponseCode.UNLOCK_ERROR);
        }
        return new BaseResponse();
    }

    /**
     * 解锁
     */
    @RequestMapping(value = "/unlock", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse unlock(@RequestBody @Valid UnlockModel unlockModel) {
        try {
            lockService.unlock(unlockModel.getLockCode(), unlockModel.getRequestId());
        } catch (Exception e) {
            logger.warn(String.format("redis解锁失败：%s", unlockModel.getLockCode()), e);
            throw new LockException(LockResponseCode.UNLOCK_ERROR);
        }
        return new BaseResponse();
    }

    /**
     * touch, 延长锁过期时间
     */
    @RequestMapping(value = "/touch", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse touch(@RequestBody LockTouchRequest touchRequest) {
        int errorCount = 0;
        if (touchRequest != null && !CollectionUtils.isEmpty(touchRequest.getLocks())) {
            for (LockModel lockModel : touchRequest.getLocks()) {
                try {
                    lockService.touch(lockModel.getLockCode(), lockModel.getRequestId(), lockModel.getExpiredTime());
                } catch (Exception e) {
                    errorCount++;
                    logger.warn(String.format("redis touch, expire失败: %s", lockModel), e);
                }
            }
        }
        if (errorCount > 0) {
            throw new LockException(LockResponseCode.TOUCH_ERROR);
        }
        return new BaseResponse();
    }
}

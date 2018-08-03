package com.destiny.lock.controller;

import com.destiny.lock.api.BaseResponse;
import com.destiny.lock.api.LockRequest;
import com.destiny.lock.api.LockTouchRequest;
import com.destiny.lock.biz.LockService;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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
        lockService.lock(lockRequest.getLockCode(), lockRequest.getRequestId(), lockRequest.getExpiredTime());
        return new BaseResponse();
    }

    /**
     * 解锁
     */
    @RequestMapping(value = "/unlock/{applicant}/{lockCode}/{requestId}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse unlock(@PathVariable("applicant") String applicant, @PathVariable("requestId") String requestId,
                               @PathVariable("lockCode") @NotEmpty @NotNull String lockCode) {
        return new BaseResponse();
    }

    /**
     * 解锁
     */
    @RequestMapping(value = "/unlock", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse unlock() {
        return new BaseResponse();
    }

    /**
     * touch, 延长锁过期时间
     */
    @RequestMapping(value = "/touch", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse touch(@RequestBody LockTouchRequest touchRequest) {
        return new BaseResponse();
    }
}

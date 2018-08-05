package com.destiny.lock.service.biz.impl;

import com.destiny.lock.api.base.LockException;
import com.destiny.lock.api.base.LockResponseCode;
import com.destiny.lock.service.biz.LockService;
import com.destiny.lock.service.redis.RedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangtianlong on 18/7/31.
 */
@Service("lock_service_redis")
public class LockServiceRedisImpl implements LockService {
    private static final Logger logger = LoggerFactory.getLogger(LockServiceRedisImpl.class);
    private static final String LOCK_PREFIX_KEY = "DISTRIBUTED_LOCK_";
    private static final String LUA_LOCK_SCRIPT =
            "local key = KEYS[1]; local ttl = KEYS[2]\n local content = KEYS[3]\n local lockSet = redis.call('setnx', "
                    + "key, content)\n if lockSet == 1 then\n redis.call('pexpire', key, ttl)\n end\n return lockSet\n";

    private static final String LUA_UNLOCK_SCRIPT =
            "local key = KEYS[1]; \n local content = KEYS[2]\n local lockValue = redis.call('GET',key)\n if lockValue"
                    + " == content then\n redis.call('DEL', key)\n end\n return nil\n";

    private static final String LUA_EXPIRE_SCRIPT =
            "local key = KEYS[1]; local ttl = KEYS[2]\n local content = KEYS[3]\n local lockValue = redis.call('GET',"
                    + "key)\n if lockValue == content then\n redis.call('pexpire', key,ttl)\n "
                    + "end\n return nil\n";
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void lock(String lockCode, String requestId, long expiredTime) {
        Long lockSet =
                (Long) redisTemplate.eval(LUA_LOCK_SCRIPT, 3, getKeyWithPrefix(lockCode), expiredTime + "", requestId);
        if (lockSet == null || lockSet != 1) {
            throw new LockException(LockResponseCode.LOCK_ERROR);
        }
    }

    @Override
    public void unlock(String lockCode, String requestId) {
        redisTemplate.eval(LUA_UNLOCK_SCRIPT, 2, getKeyWithPrefix(lockCode), requestId);
    }

    @Override
    public void touch(String lockCode, String requestId, long expiredTime) {
        redisTemplate.eval(LUA_EXPIRE_SCRIPT, 3, getKeyWithPrefix(lockCode), expiredTime + "", requestId);
    }

    private String getKeyWithPrefix(String lockCode) {
        return LOCK_PREFIX_KEY + lockCode;
    }
}

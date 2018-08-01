package com.destiny.lock.biz.ipml;

import com.destiny.lock.biz.LockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Override
    public void lock(String lockCode, String requestId, long expiredTime) {

    }

    @Override
    public void unlock(String lockCode, String requestId) {

    }

    @Override
    public void touch(String lockCode, String requestId, long expiredTime) {

    }

    private String getKeyWithPrefix(String lockCode) {
        return LOCK_PREFIX_KEY + lockCode;
    }
}

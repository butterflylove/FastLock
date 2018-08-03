package com.destiny.lock.redis;

import com.destiny.lock.api.base.LockException;
import com.destiny.lock.api.base.LockResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

@Component
public class RedisTemplate {
    private static final Logger logger = LoggerFactory.getLogger(RedisTemplate.class);
    @Autowired
    private JedisPool jedisPool;

    public Object eval(String script, int keyCount, String... params) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.eval(script, keyCount, params);
        } catch (JedisConnectionException e) {
            logger.error("jedis connection error", e);
            throw new LockException(LockResponseCode.SYSTEM_ERROR);
        } finally {
            if (jedis != null) {
                try {
                    jedis.close();
                } catch (JedisConnectionException e2) {
                    logger.error("jedis close() error", e2);
                }
            }
        }
    }
}

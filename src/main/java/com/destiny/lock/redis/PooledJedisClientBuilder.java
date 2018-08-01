package com.destiny.lock.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.util.Pool;

/**
 * jedis代理
 * Created by zhangtianlong on 18/8/1.
 */
public class PooledJedisClientBuilder {
    private static final Logger logger = LoggerFactory.getLogger(PooledJedisClientBuilder.class);
    private Pool<JedisClient> jedisPool;

    public JedisClient build() {
        if (jedisPool == null) {
            logger.error("jedisPool为空!");
            throw new RuntimeException("jedisPool为空");
        }
        return new PooledJedisClientDelegate(jedisPool).delegate();
    }

    public void setJedisPool(Pool<JedisClient> jedisPool) {
        this.jedisPool = jedisPool;
    }
}

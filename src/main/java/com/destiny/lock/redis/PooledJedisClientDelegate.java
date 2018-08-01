package com.destiny.lock.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.util.Pool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by zhangtianlong on 18/8/1.
 */
public class PooledJedisClientDelegate implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(PooledJedisClientDelegate.class);
    private Pool<JedisClient> jedisPool;

    public PooledJedisClientDelegate(Pool<JedisClient> jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisClient delegate() {
        return (JedisClient) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[] {JedisClient.class}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        JedisClient jedisClient = jedisPool.getResource();
        try {
            return method.invoke(jedisClient, args);
        } catch (Exception e) {
            logger.error("调用redis出错", e);
            throw e;
        } finally {
            if (jedisClient != null) {
                jedisPool.returnResource(jedisClient);
            }
        }
    }
}

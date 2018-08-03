package com.destiny.lock.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class BaseJedisConnPoolConfig {
    @Value("${redis.pool.maxTotal:200}")
    private int maxTotal;
    @Value("${redis.pool.maxIdle:16}")
    private int maxIdle;
    @Value("${redis.pool.minIdle:5}")
    private int minIdle;
    @Value("${redis.pool.maxWaitMillis:30}")
    private long maxWaitMillis;
    @Value("${redis.pool.LIFO:true}")
    private boolean LIFO;
    @Value("${redis.pool.testWhileIdle:false}")
    private boolean testWhileIdle;
    @Value("${redis.pool.timeBetweenEvictionRunsMillis:1000}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${redis.pool.numTestsPerEvictionRun:5}")
    private int numTestsPerEvictionRun;
    @Value("${redis.pool.testOnBorrow:true}")
    private boolean testOnBorrow;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setLifo(LIFO);
        poolConfig.setTestWhileIdle(testWhileIdle);
        poolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        poolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        return poolConfig;
    }
}

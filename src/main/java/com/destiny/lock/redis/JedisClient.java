package com.destiny.lock.redis;

import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.ScriptingCommands;

/**
 * Created by zhangtianlong on 18/7/31.
 */
public interface JedisClient extends BinaryJedisCommands, JedisCommands, ScriptingCommands {
}

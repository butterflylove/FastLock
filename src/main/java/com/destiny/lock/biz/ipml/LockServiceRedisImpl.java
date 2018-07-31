package com.destiny.lock.biz.ipml;

import com.destiny.lock.biz.LockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangtianlong on 18/7/31.
 */
public class LockServiceRedisImpl implements LockService {
    private static final Logger logger = LoggerFactory.getLogger(LockServiceRedisImpl.class);


    @Override
    public void lock(String lockCode, String requestId, long expiredTime) {

    }

    @Override
    public void unlock(String lockCode, String requestId) {

    }

    @Override
    public void touch(String lockCode, String requestId, long expiredTime) {

    }
}

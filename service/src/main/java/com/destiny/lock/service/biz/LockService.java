package com.destiny.lock.service.biz;

/**
 * Created by zhangtianlong on 18/7/31.
 */
public interface LockService {

    void lock(String lockCode, String requestId, long expiredTime);

    void unlock(String lockCode, String requestId);

    void touch(String lockCode, String requestId, long expiredTime);
}

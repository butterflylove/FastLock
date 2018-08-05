package com.destiny.lock.api;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

/**
 * Created by zhangtianlong on 18/7/28.
 */
public class LockModel {
    /**
     * 请求加锁的锁编号
     */
    @NotBlank
    private String lockCode;
    @NotBlank
    private String requestId;
    /**
     * 申请锁的过期时间
     */
    @Min(0)
    private long expiredTime;

    public LockModel() {}

    public LockModel(String lockCode, long expiredTime, String requestId) {
        this.lockCode = lockCode;
        this.expiredTime = expiredTime;
        this.requestId = requestId;
    }

    public String getLockCode() {
        return lockCode;
    }

    public void setLockCode(String lockCode) {
        this.lockCode = lockCode;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    @Override
    public String toString() {
        return "LockModel{" +
                "lockCode='" + lockCode + '\'' +
                ", requestId='" + requestId + '\'' +
                ", expiredTime=" + expiredTime +
                '}';
    }
}

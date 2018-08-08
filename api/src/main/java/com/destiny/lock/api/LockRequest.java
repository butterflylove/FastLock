package com.destiny.lock.api;

/**
 * Created by zhangtianlong on 18/7/28.
 */
public class LockRequest extends LockModel {
    /**
     * 多个锁,中间用逗号分隔
     */
    private String ownedLocks;
    /**
     * 系统名|操作名
     */
    private String applicant;
    private boolean locked;

    public String getOwnedLocks() {
        return ownedLocks;
    }

    public void setOwnedLocks(String ownedLocks) {
        this.ownedLocks = ownedLocks;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public String toString() {
        return "LockRequest{" + super.toString() +
                "ownedLocks='" + ownedLocks + '\'' +
                ", applicant='" + applicant + '\'' +
                ", locked=" + locked +
                '}';
    }
}

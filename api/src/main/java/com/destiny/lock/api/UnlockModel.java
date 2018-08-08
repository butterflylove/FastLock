package com.destiny.lock.api;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by zhangtianlong on 18/7/28.
 */
public class UnlockModel {
    @NotBlank
    private String lockCode;
    @NotBlank
    private String requestId;
    private String applicant;

    public UnlockModel() {
    }

    public UnlockModel(String applicant, String lockCode, String requestId) {
        this.lockCode = lockCode;
        this.requestId = requestId;
        this.applicant = applicant;

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

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    @Override
    public String toString() {
        return "UnlockModel{" +
                "lockCode='" + lockCode + '\'' +
                ", requestId='" + requestId + '\'' +
                ", applicant='" + applicant + '\'' +
                '}';
    }
}

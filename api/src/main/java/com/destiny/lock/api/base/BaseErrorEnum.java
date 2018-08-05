package com.destiny.lock.api.base;

public interface BaseErrorEnum {
    /**
     * 获取错误码
     */
    String getErrorCode();

    /**
     * 获取错误描述
     */
    String getErrorMsg();

    boolean getErrorFlag();
}

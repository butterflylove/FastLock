package com.destiny.lock.api.base;

public class LockException extends RuntimeException {

    protected BaseErrorEnum error;

    public LockException() {
        super();
    }

    public LockException(BaseErrorEnum error) {
        super();
        this.error = error;
    }

    /**
     * 获取错误码
     *
     * @return
     */
    public String getErrorCode() {
        if (this.getError() != null) {
            return this.getError().getErrorCode();
        }
        return null;
    }

    /**
     * 获取错误级别
     * @return
     */
    public boolean getErrorFlag(){
        if (this.getError() != null) {
            return this.getError().getErrorFlag();
        }
        return false;
    }

    /**
     * 获取错误描述
     *
     * @return
     */
    public String getErrorMsg() {
        if (this.getError() != null) {
            return this.getError().getErrorMsg();
        }
        return null;
    }

    public BaseErrorEnum getError() {
        return error;
    }

    public void setError(BaseErrorEnum error) {
        this.error = error;
    }
}

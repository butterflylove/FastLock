package com.destiny.lock.api.base;

public enum LockResponseCode implements BaseErrorEnum {
    // ============= 错误码 ===================================

    SUCCESS("00000", "成功"),

    FAIL("00001", "失败"),
    UNKNOW_ERROR("00002", "未知异常"),
    LOCK_ERROR("00003", "加锁失败"),
    ILLEGAL_REQUEST_PARAM("00004", "参数非法"),
    SYSTEM_ERROR("00005", "系统内部错误"),
    UNLOCK_ERROR("00006","解锁失败"),
    TOUCH_ERROR("00007","TOUCH有失败"),
    LOCK_PROCESSOR_NOT_EXIST("00008","锁处理器不存在"),
    NET_CALL_EXCEPTION("00009", "网络调用异常");

    private String errorCode;
    private String errorDesc;
    private boolean errorFlag;

    private LockResponseCode(String errorCode, String errorDesc) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.errorFlag = false;
    }

    private LockResponseCode(String errorCode, String errorDesc, boolean errorFlag) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.errorFlag = errorFlag;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorDesc;
    }

    @Override
    public boolean getErrorFlag() {
        return errorFlag;
    }

    @Override
    public String toString() {
        return "LockResponseCode{" +
                "errorCode='" + errorCode + '\'' +
                ", errorDesc='" + errorDesc + '\'' +
                ", errorFlag=" + errorFlag +
                '}';
    }
}

package com.destiny.lock.api;

/**
 * Created by zhangtianlong on 18/7/28.
 */
public class BaseResponse {
    /**
     * 错误码
     */
    private String result;
    /**
     * 错误信息
     */
    private String resultString;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "result='" + result + '\'' +
                ", resultString='" + resultString + '\'' +
                '}';
    }
}

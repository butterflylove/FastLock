package com.destiny.lock.api;

import java.util.List;

/**
 * Created by zhangtianlong on 18/7/28.
 */
public class LockTouchRequest {
    private List<LockModel> locks;

    public LockTouchRequest() {}

    public LockTouchRequest(List<LockModel> locks) {
        this.locks = locks;
    }

    public List<LockModel> getLocks() {
        return locks;
    }

    public void setLocks(List<LockModel> locks) {
        this.locks = locks;
    }

    @Override
    public String toString() {
        return "LockTouchRequest{" +
                "locks=" + locks +
                '}';
    }
}

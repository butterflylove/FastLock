package com.destiny.lock.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * Created by zhangtianlong on 18/8/6.
 */
public class DistributedLockToucher extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(DistributedLockToucher.class);

    private static ExecutorService getThreadPool() {
        return null;
    }

    @Override
    public void run() {

    }
}

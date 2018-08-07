package com.destiny.lock.client;

import com.destiny.lock.api.LockModel;
import com.destiny.lock.api.LockRequest;
import com.destiny.lock.api.LockTouchRequest;
import com.destiny.lock.client.rest.LockRestClient;
import org.apache.commons.lang3.tuple.MutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by zhangtianlong on 18/8/6.
 */
@Repository
public class DistributedLockToucher extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(DistributedLockToucher.class);

    private static final ExecutorService threadPool = getThreadPool();
    // touch请求的线程数
    private static final int THREAD_POOL_COUNT = 3;
    // touch请求的队列长度
    private static final int QUEUE_CAPACITY = 1000;
    // touch的每个请求包含的lockCode数量
    private static final int TOUCH_ELEMENT_COUNT = 1000;
    private volatile boolean stop;
    private long touchInterval = 1000;

    @Autowired
    private LockRestClient lockRestClient;

    public DistributedLockToucher() {
        initTouchInterval();
        this.setDaemon(true);
        this.start();
    }

    private void initTouchInterval() {
        // TODO 根据配置文件设置touch的间隔时间
    }

    private static ExecutorService getThreadPool() {
        return new ThreadPoolExecutor(THREAD_POOL_COUNT, THREAD_POOL_COUNT, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(QUEUE_CAPACITY), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                List<LockTouchRequest> lockTouchRequestList = getTouchRequestInfos();
                for (final LockTouchRequest lockTouchRequest : lockTouchRequestList) {
                    try {
                        threadPool.submit(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                lockRestClient.touch(lockTouchRequest);
                                return true;
                            }
                        });
                    } catch (Exception e) {
                        logger.warn("touch error{}:{}", lockTouchRequest, e);
                    }
                }
                // 等待间隔的时间
                try {
                    TimeUnit.MILLISECONDS.sleep(touchInterval);
                } catch (Exception e) {
                }
            } catch (Exception e) {
                logger.warn("touch error:{}", e);
            }
        }
    }

    /**
     * 获取需要touch的列表
     */
    private List<LockTouchRequest> getTouchRequestInfos() {
        List<LockTouchRequest> lockTouchRequestList = new ArrayList<LockTouchRequest>();
        List<LockModel> currentTouchLockModels = new ArrayList<LockModel>();
        for (MutablePair<Thread, LockRequest> lockRequest : DistributedLockSnapshot.ALL_LOCKS.values()) {
            if (lockRequest == null || lockRequest.getRight() == null || (!lockRequest.getRight().isLocked())) {
                continue;
            }
            currentTouchLockModels.add(lockRequest.getRight());
            if (currentTouchLockModels.size() > TOUCH_ELEMENT_COUNT) {
                lockTouchRequestList.add(new LockTouchRequest(currentTouchLockModels));
                currentTouchLockModels = new ArrayList<LockModel>();
            }
        }
        if (!CollectionUtils.isEmpty(currentTouchLockModels)) {
            lockTouchRequestList.add(new LockTouchRequest(currentTouchLockModels));
        }
        return lockTouchRequestList;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}

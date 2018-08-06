package com.destiny.lock.client;

import com.destiny.lock.api.LockRequest;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 锁快照
 */
public class DistributedLockSnapshot {
    public static final ThreadLocal<Set<String>> CURRENT_THREAD_OWNED_LOCKS = new ThreadLocal<Set<String>>();

    public static final ConcurrentHashMap<String, MutablePair<Thread, LockRequest>> ALL_LOCKS =
            new ConcurrentHashMap<String, MutablePair<Thread, LockRequest>>();
}

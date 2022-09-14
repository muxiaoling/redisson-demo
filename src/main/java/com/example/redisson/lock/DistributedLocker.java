package com.example.redisson.lock;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @author muxiaoling
 * @date 2022/9/14 20:52
 */
public interface DistributedLocker {
    RLock lock(String lockKey);

    RLock lock(String lockKey, int timeout);

    RLock lock(String lockKey, TimeUnit unit, int timeout);

    boolean tryLock(String lockKey, TimeUnit unit, long leaseTime);

    boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);
}

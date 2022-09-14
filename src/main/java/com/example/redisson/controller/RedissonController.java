package com.example.redisson.controller;

import com.example.redisson.lock.DistributedLocker;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Redisson distributed-lock 实现
 * @author muxiaoling
 * @date 2022/9/14 12:02
 */
@RestController
@Slf4j
public class RedissonController {


    @Autowired
    private DistributedLocker distributedLocker;

    /**
     * 统计抢锁线程的数量
     */
    private static int count = 0;

    private static final String LOCK_FLAG = "lock_key";

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    private static List<Integer> list = new ArrayList<>();

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentTime() {
        return simpleDateFormat.format(new Date());
    }


    @GetMapping("/lock")
    public String redissonLock() {
        threadLocal.set(++count);
        new Thread(new Runnable() {
            @Override
            public void run() {
                RLock rLock = distributedLocker.lock(LOCK_FLAG);
                log.info("{} 加锁时间：{}", Thread.currentThread().getName(), getCurrentTime());
                try {
                    list.add(threadLocal.get());
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    log.info("{} 解锁时间：{}", Thread.currentThread().getName() ,getCurrentTime());
                    distributedLocker.unlock(LOCK_FLAG);
                }
            }
        }, "小明" + threadLocal.get() + "号").start();

        return "begin";
    }

    @GetMapping("/list")
    public void print() {
        log.info(list.toString());
    }
}

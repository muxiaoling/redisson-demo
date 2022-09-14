package com.example.redisson.controller;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Redisson distributed-lock 实现
 * @author muxiaoling
 * @date 2022/9/14 12:02
 */
@RestController
@Slf4j
public class RedissonController {


    @Autowired
    private RedissonClient redissonClient;

    /**
     * 统计抢锁线程的数量
     */
    private static int count = 0;

    private final String LOCK_FLAG = "lock_key";

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getCurrentTime() {
        return simpleDateFormat.format(new Date());
    }


    @GetMapping("/lock")
    public String redissonLock() {
        count++;
        new Thread(new Runnable() {
            @Override
            public void run() {
                RLock rLock = redissonClient.getLock(LOCK_FLAG);
                log.info("{} 加锁时间：{}", Thread.currentThread().getName(), getCurrentTime());
                try {
                    rLock.lock();
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    log.info("{} 解锁时间：{}", Thread.currentThread().getName() ,getCurrentTime());
                    rLock.unlock();
                }
            }
        }, "小明" + count + "号").start();

        return "用锁数量" + count;
    }
}

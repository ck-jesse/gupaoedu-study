package com.coy.gupaoedu.study.jedis.lock;

import com.coy.gupaoedu.study.jedis.distlock.RequestIdUtil;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 简单redis分布式锁
 *
 * @author chenck
 * @date 2020/6/1 20:31
 */
public class SampleLock implements Lock {

    private Jedis jedis;

    private String lockKey;

    public SampleLock(Jedis jedis, String lockKey) {
        this.jedis = jedis;
        this.lockKey = lockKey;
    }

    // 获取锁，一直阻塞直到获取到锁
    @Override
    public void lock() {
        if (tryLock()) {
            return;
        }

        try {
            // 休眠，等待锁释放
            // 问题1：可能锁已经早就释放了，但是此处还是阻塞，是否可以在锁释放后，立即唤醒此阻塞线程去尝试再次获取锁呢？
            // 方案：通过订阅锁释放消息，以便及时唤醒被阻塞的线程再次获取锁
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 尝试再次获取锁
        lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        // 问题2：获得锁成功，假设服务挂掉，未执行unlock()，则锁一直不会释放
        // 方案：给锁设置过期时间，然后提供了一个监控锁的看门狗，它的作用是在获得锁前，不断的延长锁的有效期。
        String requestId = RequestIdUtil.requestId();
        Long result = jedis.setnx(lockKey, requestId);
        if (1 == result) {
            return true;
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        String requestId = RequestIdUtil.requestId();
        String result = jedis.set(lockKey, requestId, "NX", "PX", unit.toMillis(time));
        if ("OK".equals(result)) {
            return true;
        }
        return false;
    }

    @Override
    public void unlock() {
        // 问题3：不是锁的拥有者也可以删除锁
        // 方案：通过lua脚本，保证 查询，判断，删除 多个命令的原子性
        jedis.del(lockKey);
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}

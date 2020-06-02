package com.coy.gupaoedu.study.jedis.lock;

import com.coy.gupaoedu.study.jedis.distlock.RequestIdUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 优化版本的redis分布式锁
 *
 * @author chenck
 * @date 2020/6/1 20:31
 */
@Slf4j
public class OptimizedLock implements Lock {

    private JedisPool pool;

    private String lockKey;

    private static final Integer LOCK_TIMEOUT = 10;
    private static final Integer LOCK_EXPIRE_DELAY = 4;

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newScheduledThreadPool(3);

    private static final ConcurrentHashMap<String, ScheduledFuture> FUTURE_MAP = new ConcurrentHashMap();

    private static ThreadLocal<String> VALUE = new ThreadLocal<>();

    public OptimizedLock(JedisPool pool, String lockKey) {
        this.pool = pool;
        this.lockKey = lockKey;
    }

    // 获取锁，一直阻塞直到获取到锁
    @Override
    public void lock() {
        if (tryLock()) {
            return;
        }

        log.debug("lock fail, key={}", lockKey);
        CountDownLatch latch = new CountDownLatch(1);
        CustomJedisPubSub customJedisPubSub = new CustomJedisPubSub(latch);
        // 启动订阅监听，线程将在这里被阻塞
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 订阅锁释放消息，以便及时唤醒被阻塞的线程，
                // 注：直接订阅 lockKey 这个主题
                log.debug("lock fail, subscribe start, channel={}", lockKey);
                try (Jedis jedis = pool.getResource()) {
                    jedis.subscribe(customJedisPubSub, lockKey);
                }
                log.debug("lock fail, subscribe end, channel={}", lockKey);
            }
        }).start();

        try {
            // 设置等待时间，为了避免一直阻塞的情况出现
            latch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.debug("lock fail, await timeout, unsubscribe, channel={}", lockKey);
        customJedisPubSub.unsubscribe(lockKey);

        // 尝试再次获取锁
        lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        boolean result = false;
        try {
            // 对tryLock也进行优化，避免死锁
            result = tryLock(LOCK_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (result) {
            if (FUTURE_MAP.containsKey(lockKey)) {
                return result;
            }
            // 方案：获得锁成功，提供了一个监控锁的看门狗，不断的延长锁的有效期。
            ScheduledFuture future = EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
                try (Jedis jedis = pool.getResource()) {
                    if (jedis.exists(lockKey)) {
                        log.debug("自动续期, key={}", lockKey);
                        jedis.expire(lockKey, LOCK_TIMEOUT);// 延长锁的有效期
                    } else {
                        log.debug("锁不存在, 移除续期任务, key={}", lockKey);
                        removeScheduledFuture();// 移除续期任务
                    }
                }
            }, 1, LOCK_EXPIRE_DELAY, TimeUnit.SECONDS);

            FUTURE_MAP.put(lockKey, future);
        }
        return result;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        String requestId = RequestIdUtil.requestId();
        try (Jedis jedis = pool.getResource()) {
            String result = jedis.set(lockKey, requestId, "NX", "PX", unit.toMillis(time));
            if ("OK".equals(result)) {
                VALUE.set(requestId);
                log.debug("lock success, key={}", lockKey);
                return true;
            }
            return false;
        }
    }

    @Override
    public void unlock() {
        try (Jedis jedis = pool.getResource()) {
            // 通过lua脚本，保证 查询，判断，删除 多个命令的原子性
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(VALUE.get()));

            // 移除续期任务
            removeScheduledFuture();

            // 发布消息
            log.debug("publish unlock, key={}", lockKey);
            jedis.publish(lockKey, VALUE.get());
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }

    /**
     * 移除续期任务
     */
    private void removeScheduledFuture() {
        if (FUTURE_MAP.containsKey(lockKey)) {
            FUTURE_MAP.get(lockKey).cancel(true);
            FUTURE_MAP.remove(lockKey);
        }
    }

    @Slf4j
    public static class CustomJedisPubSub extends JedisPubSub {
        CountDownLatch latch;

        public CustomJedisPubSub(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onMessage(String channel, String message) {
            log.debug("receive message, channel={}, message={}", channel, message);
            // 唤醒等待的线程
            latch.countDown();
        }
    }
}

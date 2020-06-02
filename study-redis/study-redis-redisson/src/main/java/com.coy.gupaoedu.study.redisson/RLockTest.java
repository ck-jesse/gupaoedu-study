package com.coy.gupaoedu.study.redisson;

import org.junit.Before;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * @author chenck
 * @date 2020/6/1 14:53
 */
public class RLockTest {

    RedissonClient redisson;

    @Before
    public void before() {
        // 1. Create config object
        Config config = new Config();
        //config.useClusterServers().addNodeAddress("redis://127.0.0.1:6379");// 集群模式
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");// 单实例模式

        // 2. Create Redisson instance
        // Sync and Async API
        RedissonClient redisson = Redisson.create(config);

//        // Reactive API
//        RedissonReactiveClient redissonReactive = Redisson.createReactive(config);
//
//        // RxJava2 API
//        RedissonRxClient redissonRx = Redisson.createRx(config);
    }

    /**
     * lock() 本质还是设置了过期时间，然后通过一个监控锁的看门狗，来不断的延长锁的有效期。
     * 自定义基于redis的分布式锁也可以参考该方式来实现，主要是实现思想
     */
    @Test
    public void rlockTest() {
        RLock lock = redisson.getLock("lock");
        lock.lock();
    }

}

package com.coy.gupaoedu.study.jedis.lock;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 * @author chenck
 * @date 2020/6/2 13:43
 */
public class SampleLockTest {

    SampleLock lock;

    @Before
    public void before() {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        lock = new SampleLock(jedis, "sample_lock");
    }


    @Test
    public void lockTest() throws InterruptedException {
        lock.lock();
        int i = 0;
        while (true) {
            i++;
            Thread.sleep(1000);
            if (i == 30) {
                lock.unlock();
            }
            if (i == 35) {
                break;
            }
        }
    }

    @Test
    public void tryLockTest() {
        boolean result = lock.tryLock();
        System.out.println(result);
        while (true) {
        }
    }

    @Test
    public void tryLockTimeoutTest() throws InterruptedException {
        boolean result = lock.tryLock(30, TimeUnit.SECONDS);
        System.out.println(result);
    }
}

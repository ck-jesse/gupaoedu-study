package com.coy.gupaoedu.study.jedis.lock;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisPool;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @author chenck
 * @date 2020/6/2 10:11
 */
public class OptimizedLockTest {

    OptimizedLock lock;

    @Before
    public void before() {
        // log4j 加载
        URL url = OptimizedLockTest.class.getClassLoader().getResource("log4j.properties");
        PropertyConfigurator.configure(url.getPath());

        // 因为普通的命令和发布订阅不能使用同一个jedis实例，所以采用JedisPool
        // 特别注意JedisPool获取Jedis实例后，需要释放Jedis实例，否则会Jedis资源不足时，JedisPool.getResource()会阻塞
        JedisPool pool = new JedisPool("127.0.0.1", 6379);
        lock = new OptimizedLock(pool, "optimized_lock");
    }

    // lock0 - 获取锁，自动续期，解锁时通知其他被阻塞的获取锁的线程
    @Test
    public void lockTest() throws InterruptedException {
        lock.lock();
        unlockTest();
    }

    // lock1 - 获取锁，自动续期，解锁时通知其他被阻塞的获取锁的线程
    @Test
    public void lockTest1() throws InterruptedException {
        lock.lock();
        unlockTest();
    }

    // lock2 - 获取锁，自动续期，解锁时通知其他被阻塞的获取锁的线程
    @Test
    public void lockTest2() throws InterruptedException {
        lock.lock();
        unlockTest();
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


    private void unlockTest() throws InterruptedException {
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
}

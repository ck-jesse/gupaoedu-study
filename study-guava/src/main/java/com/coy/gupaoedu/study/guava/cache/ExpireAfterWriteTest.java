package com.coy.gupaoedu.study.guava.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 定时回收：expireAfterAccess 和 expireAfterWrite
 *
 * @author chenck
 * @date 2019/10/16 11:02
 */
public class ExpireAfterWriteTest {

    // 这是一个本地缓存，guava提供的cache是一个简洁、高效，易于维护的。
    // 为什么这么说呢？因为并没有一个单独的线程用于刷新 OR 清理cache，对于cache的操作，都是通过访问/读写带来的，也就是说在读写中完成缓存的刷新操作！
    private static LoadingCache<Integer, AtomicInteger> cache = CacheBuilder.newBuilder()
            .initialCapacity(10)
            .maximumSize(10)
            //.expireAfterAccess(5, TimeUnit.SECONDS)// 缓存项在给定时间内没有被读/写访问,则回收
            .expireAfterWrite(3, TimeUnit.SECONDS)// 缓存项在给定时间内没有被写访问(创建或覆盖),则回收
            .build(new CacheLoader<Integer, AtomicInteger>() {
                @Override
                public AtomicInteger load(Integer key) throws Exception {
                    return new AtomicInteger(-1);
                }
            });

    @Before
    public void init() throws Exception {
        // 初始写入数据
        for (int i = 0; i < 10; i++) {
            cache.put(i, new AtomicInteger(0));
            // 此处刚put进去，所以get是不会破坏
            //System.out.println("key=" + i + ",value=" + cache.get(i));
        }
        System.out.println("InitCacheSize=" + cache.size());
        System.out.println("InitCache=" + cache.asMap());
    }

    /**
     * LoadingCache.get(key)
     * 当key不存在与cache中时，会将cache中queue的头部元素替换为这个不存在的key，
     * 并且其值通过执行 com.google.common.cache.CacheLoader.load() 方法来获取并设置到cache中
     */
    @Test
    public void getTest() throws Exception {
        System.out.println();
        System.out.println("key=" + 20 + ",value=" + cache.get(20));
        System.out.println(cache.asMap());
        System.out.println("key=" + 21 + ",value=" + cache.get(21));
        System.out.println(cache.asMap());
        System.out.println();

        System.out.println("从cache中无损获取数据");
        for (Map.Entry entry : cache.asMap().entrySet()) {
            System.out.println("key=" + entry.getKey() + ",value=" + entry.getValue());
        }

        System.out.println("从cache中有损获取数据 - 会将不存在的key设置到queue头部替换掉原来的元素");
        for (int i = 9; i >= 0; i--) {
            System.out.println("key=" + i + ",value=" + cache.get(i));
        }
    }

    /**
     * 过期测试
     * 在key写入后5秒内,如果该key没有再写入数据则过期
     */
    @Test
    public void expireAfterWriteTest() throws InterruptedException {
        // 打印数据
        Thread printThread = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    System.out.println(i + " print before" + cache.asMap());
                    Thread.sleep(2000);
                    System.out.println(i + " print after" + cache.asMap());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 写入数据
        Thread putThread = new Thread(() -> {
            try {
                int index = 100;
                for (int i = 0; i < 2; i++) {
                    System.out.println(i + " put before" + cache.asMap());
                    Thread.sleep(1000);
                    cache.put(1, new AtomicInteger(index));
                    cache.put(2, new AtomicInteger(index));
                    cache.put(3, new AtomicInteger(index));
                    System.out.println(i + " put after" + cache.asMap());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        printThread.start();
        putThread.start();
        printThread.join();
        putThread.join();

    }
}

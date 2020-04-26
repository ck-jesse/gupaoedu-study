package com.coy.gupaoedu.study.guava.cache;

import com.ck.platform.common.util.DateUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 场景：缓存30分钟内最新50个下单用户的信息，用于首页轮播
 * <p>
 * expireAfterAccess
 *
 * @author chenck
 * @date 2020/3/19 10:50
 */
public class UserCacheTest {

    // 创建移除监听器，以便缓存项被移除时做一些额外操作
    // 监听器方法是在移除缓存是同步调用的，因为缓存的维护和请求响应通常是同时进行的,代价高昂的监听器方法在同步模式下会拖慢正常的缓存请求。
    // 在这种情况下,你可以使用 RemovalListeners.asynchronous(RemovalListener, Executor)把监听器装饰为异步操作。
    public static RemovalListener<String, User> removalListener = new RemovalListener<String, User>() {
        @Override
        public void onRemoval(RemovalNotification<String, User> notification) {
            System.out.println(String.format("%s 触发移除，%s", DateUtils.getCurrentDateYYYYMMDDHHMMSS(), notification.getValue()));
        }
    };

    // 创建缓存
    public static LoadingCache<String, User> cache = CacheBuilder.newBuilder()
            .maximumSize(5)// 最多5个
            .expireAfterWrite(5, TimeUnit.SECONDS)// 缓存项在给定时间内没有被写访问(创建或覆盖),则回收
            .recordStats() // 开启统计信息
            .removalListener(removalListener)
            .build(new CacheLoader<String, User>() {
                @Override
                public User load(String key) throws Exception {
                    // 此处不产生用户信息，轮播的场景是批量获取
                    System.out.println(String.format("%s 触发加载，userId=%s", DateUtils.getCurrentDateYYYYMMDDHHMMSS(), key));
                    return null;
                }
            });

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Test
    public void test() {
        // 模拟：用户下单成功时，往缓存中添加用户信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    User user = User.init();
                    cache.put(user.getUserId(), user);
                    System.out.println(String.format("%s 添加缓存，userId=%s", DateUtils.getCurrentDateYYYYMMDDHHMMSS(), user.getUserId()));
                    i++;
                    if (i >= 10) {
                        System.out.println(String.format("%s 添加缓存，退出", DateUtils.getCurrentDateYYYYMMDDHHMMSS()));
                        break;
                    }
                }
            }
        }).start();

        // 模拟：首页定时查询用户列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(cache.stats());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(String.format("%s 查询缓存，size=%d, %s", DateUtils.getCurrentDateYYYYMMDDHHMMSS(), cache.size(),
                            cache.asMap().keySet()));
                }
            }
        }).start();
        // 异步清理缓存
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(String.format("%s 异步清理缓存开始，size=%d, %s", DateUtils.getCurrentDateYYYYMMDDHHMMSS(), cache.size(),
                        cache.asMap().keySet()));
                cache.cleanUp();
                System.out.println(String.format("%s 异步清理缓存结束，size=%d, %s", DateUtils.getCurrentDateYYYYMMDDHHMMSS(), cache.size(),
                        cache.asMap().keySet()));
            }
        }, 10, 1, TimeUnit.SECONDS);

        // hold住主线程
        while (true) {
        }
    }
}

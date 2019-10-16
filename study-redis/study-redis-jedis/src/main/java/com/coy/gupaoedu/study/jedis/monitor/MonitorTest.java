package com.coy.gupaoedu.study.jedis.monitor;

import com.google.common.util.concurrent.AtomicLongMap;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisMonitor;

/**
 * 可以监控redis服务端的执行命令，如分析热点数据等场景
 *
 * 监控会消耗redis服务端的性能，同时监控只能针对单机进行监控，不支持集群监控
 *
 * 可以通过facebook开源的 redis-faina 来分析热点数据
 *
 * @Author: qingshan
 * @Date: 2019/9/28 21:36
 * @Description: 咕泡学院，只为更好的你
 */
public class MonitorTest {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //获取10万条命令
        jedis.monitor(new JedisMonitor() {
            @Override
            public void onCommand(String command) {
                System.out.println("#monitor: " + command);
                AtomicLongMap<String> ATOMIC_LONG_MAP = AtomicLongMap.create();
                // ATOMIC_LONG_MAP.incrementAndGet(command);
            }
        });

    }
}

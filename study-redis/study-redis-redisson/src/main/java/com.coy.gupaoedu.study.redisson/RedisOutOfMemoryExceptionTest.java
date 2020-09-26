package com.coy.gupaoedu.study.redisson;

import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author chenck
 * @date 2020/9/25 16:59
 */
public class RedisOutOfMemoryExceptionTest {

    @Test
    public void testJedis() {
        int index = 0;
        System.out.println("jedis case");
//            Jedis jedis = new Jedis("localhost", 6379);
        Jedis jedis = new Jedis("wg-colorstone.redis.rds.aliyuncs.com", 6379);
        jedis.auth("W4oj06#rj*7qCjLq");
        jedis.select(1);
        do {
            index++;
            try {
                jedis.setex("Chemical:" + index, 3 * 60, UUID.randomUUID().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (true);
    }

    @Test
    public void testJedisPool() {
        int index = 0;
        System.out.println("jedis pool case");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(1000);
        poolConfig.setMaxIdle(32);
        poolConfig.setMaxWaitMillis(100 * 1000);
        poolConfig.setTestOnBorrow(true);

        JedisPool jedisPool = new JedisPool(poolConfig, "wg-colorstone.redis.rds.aliyuncs.com", 6379, 3000, "W4oj06#rj*7qCjLq");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                Jedis jedis = jedisPool.getResource();
                try {
                    jedis.setex("Chemical:" + index, 3 * 60, UUID.randomUUID().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    jedis.close();
                }
            });
        }
        while (true) {

        }
    }

    @Test
    public void testRedisson() {
        int index = 0;
        System.out.println("redisson case " + System.currentTimeMillis());
        Config config = new Config();
//            config.useSingleServer().setAddress("redis://localhost:6379").setDatabase(database);
        config.useSingleServer().setAddress("redis://wg-colorstone.redis.rds.aliyuncs.com:6379")
                .setPassword("W4oj06#rj*7qCjLq").setDatabase(1);
        RedissonClient client = Redisson.create(config);
        do {
            index++;
            try {
                RMap<String, String> rMap = client.getMap("Chemical:" + index);
                rMap.put(String.valueOf(index), UUID.randomUUID().toString());
                rMap.expire(3L, TimeUnit.MINUTES);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (true);
    }
}

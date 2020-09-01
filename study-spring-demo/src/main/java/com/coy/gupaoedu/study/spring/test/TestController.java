package com.coy.gupaoedu.study.spring.test;

import com.coy.gupaoedu.study.spring.cache.User;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证 RestTemplate 和 RedissonClient 序列化和反序列化的问题
 *
 * @author chenck
 * @date 2020/9/1 9:54
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @RequestMapping(value = "/redisSet")
    public String redisSet(String key) {
        User user = new User();
        user.setName("test");
        user.setAddr(key);
        user.setCurrTime(System.currentTimeMillis());

        redisTemplate.opsForValue().set(key, user);
        return "succ";
    }

    @RequestMapping(value = "/redisSetValue")
    public String redisSet(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        return "succ";
    }

    @RequestMapping(value = "/redissonGet")
    public String redissonGet(String key) {
        Object value = redissonClient.getBucket(key).get();
        System.out.println(value);
        return "succ";
    }

}

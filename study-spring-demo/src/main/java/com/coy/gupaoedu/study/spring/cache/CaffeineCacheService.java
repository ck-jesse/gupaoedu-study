package com.coy.gupaoedu.study.spring.cache;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenck
 * @date 2020/4/26 19:37
 */
@Service
public class CaffeineCacheService {

    @Cacheable(value = "queryUser", key = "#userId")
    public String queryUser(String userId) {
        return System.currentTimeMillis() + " " + userId;
    }

    @Cacheable(value = "queryUserList", key = "#userId", sync = true)
    public List<String> queryUserList(String userId) {
        List<String> list = new ArrayList<>();
        list.add(System.currentTimeMillis() + " 1 " + userId);
        list.add(System.currentTimeMillis() + " 2 " + userId);
        return list;
    }
}

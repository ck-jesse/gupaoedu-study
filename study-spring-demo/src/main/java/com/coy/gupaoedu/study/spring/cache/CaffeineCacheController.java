package com.coy.gupaoedu.study.spring.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenck
 * @date 2020/4/26 19:40
 */
@Slf4j
@RestController
public class CaffeineCacheController {

    @Autowired
    CaffeineCacheService caffeineCacheService;

    @RequestMapping(value = "/queryUser")
    public String queryUser(String userId) {
        return caffeineCacheService.queryUser(userId);
    }

    @RequestMapping(value = "/queryUserList")
    public List<String> queryUserList(String userId) {
        return caffeineCacheService.queryUserList(userId);
    }
}

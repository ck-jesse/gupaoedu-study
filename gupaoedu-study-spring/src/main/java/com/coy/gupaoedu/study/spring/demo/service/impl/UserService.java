package com.coy.gupaoedu.study.spring.demo.service.impl;

import com.coy.gupaoedu.study.spring.demo.service.IDemoService;
import com.coy.gupaoedu.study.spring.framework.mvc.annotation.GPAutowired;
import com.coy.gupaoedu.study.spring.framework.mvc.annotation.GPService;

/**
 * 用于测试基于类的CGLIB代理
 */
@GPService
public class UserService {

    @GPAutowired
    private IDemoService demoService;

    public String getName(String name) {
        String result = demoService.get(name);
        return result;
    }

}

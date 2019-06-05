package com.coy.gupaoedu.study.spring.demo.service.impl;

import com.coy.gupaoedu.study.spring.demo.service.IDemoService;
import com.coy.gupaoedu.study.spring.framework.beans.annotation.GPAutowired;
import com.coy.gupaoedu.study.spring.framework.context.annotation.GPService;

/**
 * 用于测试基于类的CGLIB代理
 */
@GPService
public class UserService {

    /**
     * 循环依赖
     */
    @GPAutowired
    private IDemoService demoService;

    public String getName(String name) {
        String result = demoService.get(name);
        return result;
    }

}

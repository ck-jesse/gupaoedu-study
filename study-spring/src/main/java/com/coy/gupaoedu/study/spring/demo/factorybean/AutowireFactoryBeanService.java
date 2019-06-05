package com.coy.gupaoedu.study.spring.demo.factorybean;

import com.coy.gupaoedu.study.spring.framework.beans.annotation.GPAutowired;
import com.coy.gupaoedu.study.spring.framework.context.annotation.GPService;

/**
 * 测试自动注入 自定义FactoryBean创建的bean
 */
@GPService
public class AutowireFactoryBeanService {

    /**
     * 注入自定义FactoryBean创建的bean
     */
    @GPAutowired
    private FactoryBeanService factoryBeanService;

    public String getName(String name) {
        String result = factoryBeanService.test(name);
        return result;
    }

}

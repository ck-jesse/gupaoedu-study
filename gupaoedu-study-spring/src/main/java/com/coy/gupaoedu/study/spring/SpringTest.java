package com.coy.gupaoedu.study.spring;

import com.coy.gupaoedu.study.spring.demo.service.IDemoService;
import com.coy.gupaoedu.study.spring.demo.service.impl.DemoService;
import com.coy.gupaoedu.study.spring.framework.context.GPApplicationContext;
import com.coy.gupaoedu.study.spring.framework.context.support.GPAbstractApplicationContext;

/**
 * @author chenck
 * @date 2019/4/19 16:10
 */
public class SpringTest {

    public static void main(String[] args) {
        String configLoactions = "application.properties";
        GPApplicationContext context = new GPAbstractApplicationContext(configLoactions);
        IDemoService demoService = context.getBean(DemoService.class);
        System.out.println(demoService.get("perfect"));
    }
}

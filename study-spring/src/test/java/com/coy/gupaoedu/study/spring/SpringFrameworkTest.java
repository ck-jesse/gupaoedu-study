package com.coy.gupaoedu.study.spring;

import com.coy.gupaoedu.study.spring.demo.service.DemoService;
import com.coy.gupaoedu.study.spring.demo.service.impl.DemoServiceImpl;
import com.coy.gupaoedu.study.spring.demo.service.impl.UserService;
import com.coy.gupaoedu.study.spring.framework.context.GPApplicationContext;
import com.coy.gupaoedu.study.spring.framework.context.support.GPAbstractApplicationContext;
import org.junit.Test;

/**
 * @author chenck
 * @date 2019/4/19 16:10
 */
public class SpringFrameworkTest {

    /**
     * 仿写spring框架的测试
     */
    @Test
    public void springFrameworkTest() throws Exception {
        String configLoactions = "application.properties";
        GPApplicationContext context = new GPAbstractApplicationContext(configLoactions);

        Thread.sleep(5000);
        DemoService demoService = context.getBean(DemoServiceImpl.class);
        System.out.println(demoService.get("demoService"));

        //TwoAction twoAction = context.getBean(TwoAction.class);
        //System.out.println(twoAction.getName("TwoAction"));

        UserService userService = context.getBean(UserService.class);
        System.out.println(userService.getName("AutowireFactoryBeanService"));
    }

}

package com.coy.gupaoedu.study.spring;

import com.coy.gupaoedu.study.spring.demo.factorybean.AutowireFactoryBeanService;
import com.coy.gupaoedu.study.spring.demo.factorybean.CustomFactoryBean;
import com.coy.gupaoedu.study.spring.demo.factorybean.FactoryBeanService;
import com.coy.gupaoedu.study.spring.framework.context.GPApplicationContext;
import com.coy.gupaoedu.study.spring.framework.context.support.GPAbstractApplicationContext;
import org.junit.Test;

/**
 * @author chenck
 * @date 2019/6/5 10:36
 */
public class CustomFactoryBeanTest {

    /**
     * 自定义FacotryBean测试
     */
    @Test
    public void customFactoryBeanTest() throws Exception {
        String configLoactions = "application.properties";
        String scanPackage = "com.coy.gupaoedu.study.spring.demo.factorybean";
        GPApplicationContext context = new GPAbstractApplicationContext(scanPackage, configLoactions);

        Thread.sleep(5000);
        // 获取自定义FactoryBean的
        FactoryBeanService factoryBeanService = (FactoryBeanService) context.getBean(CustomFactoryBean.class);
        factoryBeanService.test("FactoryBeanService test");

        Thread.sleep(3000);
        AutowireFactoryBeanService autowireFactoryBeanService = (AutowireFactoryBeanService) context.getBean(AutowireFactoryBeanService.class);
        System.out.println(autowireFactoryBeanService.getName("my name is yyy"));
    }
}

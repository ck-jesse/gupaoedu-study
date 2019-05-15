package com.coy.gupaoedu.study.spring.demo.processor;

import com.coy.gupaoedu.study.spring.demo.service.IDemoService;
import com.coy.gupaoedu.study.spring.framework.beans.factory.config.GPBeanPostProcessor;

/**
 * @author chenck
 * @date 2019/4/30 9:58
 */
public class DemoBeanPostProcessor implements GPBeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof IDemoService) {
            System.out.println("DemoBeanPostProcessor before " + bean);
            System.out.println("DemoBeanPostProcessor before " + bean.getClass().toString());
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof IDemoService) {
            System.out.println("DemoBeanPostProcessor after " + bean);
            System.out.println("DemoBeanPostProcessor after " + bean.getClass().toString());
        }
        return bean;
    }
}

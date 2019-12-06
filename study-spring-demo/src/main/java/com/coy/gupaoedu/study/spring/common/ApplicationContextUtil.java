package com.coy.gupaoedu.study.spring.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ApplicationContext Util
 *
 * @author chenck
 * @date 2019/10/18 10:17
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;

    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        CONTEXT = context;
    }

    public static ApplicationContext getApplicationContext() {
        return CONTEXT;
    }

    public static Object getBean(String name) {
        return CONTEXT.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return CONTEXT.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return CONTEXT.getBean(requiredType);
    }
}

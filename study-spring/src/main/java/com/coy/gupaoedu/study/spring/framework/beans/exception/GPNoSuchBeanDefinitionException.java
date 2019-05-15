package com.coy.gupaoedu.study.spring.framework.beans.exception;

/**
 * @author chenck
 * @date 2019/4/25 16:11
 */
public class GPNoSuchBeanDefinitionException extends GPBeansException {

    private String beanName;

    private Class<?> beanType;

    public GPNoSuchBeanDefinitionException(String name) {
        super("No bean named '" + name + "' available");
        this.beanName = name;
    }

    public GPNoSuchBeanDefinitionException(String name, String message) {
        super("No bean named '" + name + "' available: " + message);
        this.beanName = name;
    }

    public GPNoSuchBeanDefinitionException(Class<?> type) {
        super("No qualifying bean of type '" + type + "' available");
        this.beanType = type;
    }

    public GPNoSuchBeanDefinitionException(Class<?> type, String message) {
        super("No qualifying bean of type '" + type + "' available: " + message);
        this.beanType = type;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class<?> getBeanType() {
        return beanType;
    }

    public void setBeanType(Class<?> beanType) {
        this.beanType = beanType;
    }
}

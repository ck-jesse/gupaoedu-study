package com.coy.gupaoedu.study.client.rpc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 依赖的bean的定义
 * <p>
 * 基于FactoryBean来自定义bean的创建过程
 *
 * @author chenck
 * @date 2019/6/9 16:19
 */
public class RefereceBean<T> implements FactoryBean, ApplicationContextAware {

    private String version;
    private Class<T> targetType;

    private T singletonObject;
    private ApplicationContext applicationContext;

    public RefereceBean(Class<T> clazz, String version) {
        this.targetType = clazz;
        this.version = version;
    }

    public RefereceBean(Class<T> clazz) {
        this(clazz, null);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Object getObject() throws Exception {
        RpcProxyClient rpcProxyClient = applicationContext.getBean(RpcProxyClient.class);
        if (isSingleton()) {
            if (null != singletonObject) {
                return singletonObject;
            }
            singletonObject = rpcProxyClient.clientProxy(targetType, version);
            return singletonObject;
        } else {
            return rpcProxyClient.clientProxy(targetType, version);
        }
    }

    @Override
    public Class<?> getObjectType() {
        return targetType;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

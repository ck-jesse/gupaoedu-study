package com.coy.gupaoedu.study.client.rpc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 依赖的bean的定义
 *
 * @author chenck
 * @date 2019/6/9 16:19
 */
public class RefereceBean<T> implements FactoryBean, ApplicationContextAware {

    private String host;
    private int port;
    private String version;
    private Class<T> targetType;

    private T singletonObject;
    private ApplicationContext applicationContext;

    public RefereceBean(Class<T> clazz, String host, int port, String version) {
        this.targetType = clazz;
        this.host = host;
        this.port = port;
        this.version = version;
    }

    public RefereceBean(Class<T> clazz, String host, int port) {
        this(clazz, host, port, null);
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
            singletonObject = rpcProxyClient.clientProxy(targetType, host, port, version);
            return singletonObject;
        } else {
            return rpcProxyClient.clientProxy(targetType, host, port);
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

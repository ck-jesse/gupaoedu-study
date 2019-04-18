package com.coy.gupaoedu.study.spring.framework.aop.framework;

import java.io.Serializable;
import java.lang.reflect.Proxy;

/**
 * AOP代理工厂
 * 创建CGLIB代理或JDK动态代理
 *
 * @author chenck
 * @date 2019/4/18 11:50
 */
public class GPAopProxyFactory implements Serializable {

    /**
     * 创建aop代理对象
     */
    public GPAopProxy createAopProxy(GPAdvisedSupport config) throws AopConfigException {
        // 代理类 或者 代理目标的接口集为空则使用cglib代理
        if (config.isProxyTargetClass() || config.getProxiedInterfaces().length == 0) {
            Class<?> targetClass = config.getTargetClass();
            if (targetClass == null) {
                throw new AopConfigException("TargetSource cannot determine target class: " +
                        "Either an interface or a target is required for proxy creation.");
            }
            if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
                return new GPJdkDynamicAopProxy(config);
            }
            return new GPCglibAopProxy(config);
        } else {
            return new GPJdkDynamicAopProxy(config);
        }
    }

}

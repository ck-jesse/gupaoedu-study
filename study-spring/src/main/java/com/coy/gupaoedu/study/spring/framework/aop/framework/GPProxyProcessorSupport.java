package com.coy.gupaoedu.study.spring.framework.aop.framework;


import com.coy.gupaoedu.study.spring.framework.beans.GPAware;
import com.coy.gupaoedu.study.spring.framework.beans.GPDisposableBean;
import com.coy.gupaoedu.study.spring.framework.beans.GPInitializingBean;
import com.coy.gupaoedu.study.spring.framework.core.util.ClassUtils;
import com.coy.gupaoedu.study.spring.framework.core.util.ObjectUtils;

import java.io.Closeable;

/**
 * 具有代理处理器通用功能的基类
 *
 * @author chenck
 * @date 2019/4/16 19:48
 */
public class GPProxyProcessorSupport extends GPProxyConfig {

    /**
     * 类加载器，默认值为当前线程的类加载器
     */
    private ClassLoader proxyClassLoader = Thread.currentThread().getContextClassLoader();

    public void setProxyClassLoader(ClassLoader classLoader) {
        this.proxyClassLoader = classLoader;
    }

    protected ClassLoader getProxyClassLoader() {
        return this.proxyClassLoader;
    }

    /**
     * 检查指定bean类实现的接口，并将它们设置到ProxyFactory中
     *
     * @param beanClass    the class of the bean
     * @param proxyFactory the ProxyFactory for the bean
     */
    protected void evaluateProxyInterfaces(Class<?> beanClass, GPProxyFactory proxyFactory) {
        Class<?>[] targetInterfaces = ClassUtils.getAllInterfacesForClass(beanClass, getProxyClassLoader());
        boolean hasReasonableProxyInterface = false;
        for (Class<?> ifc : targetInterfaces) {
            if (!isConfigurationCallbackInterface(ifc) && ifc.getMethods().length > 0) {
                hasReasonableProxyInterface = true;
                break;
            }
        }
        if (hasReasonableProxyInterface) {
            // Must allow for introductions; can't just set interfaces to the target's interfaces only.
            for (Class<?> ifc : targetInterfaces) {
                proxyFactory.addInterface(ifc);
            }
        } else {
            proxyFactory.setProxyTargetClass(true);
        }
    }

    /**
     * 确定指定接口是否只是一个容器回调，如果是，则不将其作为一个合格的代理接口
     *
     * @param ifc the interface to check
     * @return whether the given interface is just a container callback
     */
    protected boolean isConfigurationCallbackInterface(Class<?> ifc) {
        return (GPInitializingBean.class == ifc || GPDisposableBean.class == ifc || Closeable.class == ifc ||
                AutoCloseable.class == ifc || ObjectUtils.containsElement(ifc.getInterfaces(), GPAware.class));
    }
}

package com.coy.gupaoedu.study.spring.framework.beans.support;

import com.coy.gupaoedu.study.spring.framework.beans.GPBeanDefinition;
import com.coy.gupaoedu.study.spring.framework.beans.GPDisposableBean;
import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeansException;
import com.coy.gupaoedu.study.spring.framework.core.util.BeanUtils;
import com.coy.gupaoedu.study.spring.framework.core.util.ReflectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author chenck
 * @date 2019/4/18 17:47
 */
public class GPDisposableBeanAdapter implements GPDisposableBean, Runnable, Serializable {

    private static final Log logger = LogFactory.getLog(GPDisposableBeanAdapter.class);

    private final Object bean;

    private final String beanName;

    private String destroyMethodName;

    private transient Method destroyMethod;

    private final boolean invokeDisposableBean;

    public GPDisposableBeanAdapter(Object bean, String beanName, GPBeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.invokeDisposableBean = (this.bean instanceof GPDisposableBean);
        String destroyMethodName = beanDefinition.getDestroyMethodName();
        if (destroyMethodName != null && !(this.invokeDisposableBean && "destroy".equals(destroyMethodName))) {
            this.destroyMethodName = destroyMethodName;
            // 根据方法名找到对应的Method
            this.destroyMethod = BeanUtils.findMethodWithMinimalParameters(this.bean.getClass().getMethods(), destroyMethodName);
            if (this.destroyMethod == null) {
                throw new GPBeansException("Couldn't find a destroy method named '" +
                        destroyMethodName + "' on bean with name '" + beanName + "'");
            } else {
                Class<?>[] paramTypes = this.destroyMethod.getParameterTypes();
                if (paramTypes.length > 1) {
                    throw new GPBeansException("Method '" + destroyMethodName + "' of bean '" +
                            beanName + "' has more than one parameter - not supported as destroy method");
                } else if (paramTypes.length == 1 && boolean.class != paramTypes[0]) {
                    throw new GPBeansException("Method '" + destroyMethodName + "' of bean '" +
                            beanName + "' has a non-boolean parameter - not supported as destroy method");
                }
            }
        }
    }

    @Override
    public void destroy() throws Exception {

        if (this.invokeDisposableBean) {
            if (logger.isDebugEnabled()) {
                logger.debug("Invoking destroy() on bean with name '" + this.beanName + "'");
            }
            try {
                ((GPDisposableBean) bean).destroy();
            } catch (Throwable ex) {
                String msg = "Invocation of destroy method failed on bean with name '" + this.beanName + "'";
                if (logger.isDebugEnabled()) {
                    logger.warn(msg, ex);
                } else {
                    logger.warn(msg + ": " + ex);
                }
            }
        }

        if (this.destroyMethod != null) {
            invokeCustomDestroyMethod(this.destroyMethod);
        } else if (this.destroyMethodName != null) {
            Method methodToCall = BeanUtils.findMethodWithMinimalParameters(this.bean.getClass().getMethods(), this.destroyMethodName);
            if (methodToCall != null) {
                invokeCustomDestroyMethod(methodToCall);
            }
        }
    }

    /**
     * 执行销毁方法
     */
    private void invokeCustomDestroyMethod(final Method destroyMethod) {
        Class<?>[] paramTypes = destroyMethod.getParameterTypes();
        final Object[] args = new Object[paramTypes.length];
        if (paramTypes.length == 1) {
            args[0] = Boolean.TRUE;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Invoking destroy method '" + this.destroyMethodName +
                    "' on bean with name '" + this.beanName + "'");
        }
        try {
            ReflectionUtils.makeAccessible(destroyMethod);
            destroyMethod.invoke(bean, args);
        } catch (InvocationTargetException ex) {
            String msg = "Invocation of destroy method '" + this.destroyMethodName +
                    "' failed on bean with name '" + this.beanName + "'";
            if (logger.isDebugEnabled()) {
                logger.warn(msg, ex.getTargetException());
            } else {
                logger.warn(msg + ": " + ex.getTargetException());
            }
        } catch (Throwable ex) {
            logger.error("Couldn't invoke destroy method '" + this.destroyMethodName +
                    "' on bean with name '" + this.beanName + "'", ex);
        }
    }

    @Override
    public void run() {
        try {
            destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

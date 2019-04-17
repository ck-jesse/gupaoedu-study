package com.coy.gupaoedu.study.spring.framework.aop.framework.autoproxy;

import com.coy.gupaoedu.study.spring.framework.aop.framework.GPProxyProcessorSupport;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactoryAware;
import com.coy.gupaoedu.study.spring.framework.beans.GPFactoryBean;
import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeansException;
import com.coy.gupaoedu.study.spring.framework.beans.factory.config.GPInstantiationAwareBeanPostProcessor;
import com.coy.gupaoedu.study.spring.framework.core.util.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用AOP代理包装每个符合条件的bean的实现，在调用bean本身之前将其委托给指定的拦截器
 *
 * @author chenck
 * @date 2019/4/16 20:01
 */
public class GPAbstractAutoProxyCreator extends GPProxyProcessorSupport implements GPInstantiationAwareBeanPostProcessor, GPBeanFactoryAware {

    private GPBeanFactory beanFactory;

    /**
     * 早期的代理引用
     */
    private final Set<Object> earlyProxyReferences = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    private final Map<Object, Boolean> advisedBeans = new ConcurrentHashMap<>(256);

    @Override
    public void setBeanFactory(GPBeanFactory beanFactory) throws GPBeansException {
        this.beanFactory = beanFactory;
    }

    public GPBeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * Create a proxy with the configured interceptors if the bean is
     * identified as one to proxy by the subclass.
     *
     * @see #getAdvicesAndAdvisorsForBean
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean != null) {
            Object cacheKey = getCacheKey(bean.getClass(), beanName);
            if (!this.earlyProxyReferences.contains(cacheKey)) {
                return wrapIfNecessary(bean, beanName, cacheKey);
            }
        }
        return bean;
    }

    /**
     * TODO
     * 如果给定的bean符合被代理的条件，就将其包装起来
     */
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        /*if (StringUtils.hasLength(beanName) && this.targetSourcedBeans.contains(beanName)) {
            return bean;
        }
        if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
            return bean;
        }
        if (isInfrastructureClass(bean.getClass()) || shouldSkip(bean.getClass(), beanName)) {
            this.advisedBeans.put(cacheKey, Boolean.FALSE);
            return bean;
        }

        // Create proxy if we have advice.
        // AOP处理：如果有advice，则创建代理
        // 获取bean匹配的AOP拦截器
        Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
        if (specificInterceptors != null) {
            this.advisedBeans.put(cacheKey, Boolean.TRUE);
            Object proxy = createProxy(bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
            this.proxyTypes.put(cacheKey, proxy.getClass());
            return proxy;
        }*/

        this.advisedBeans.put(cacheKey, Boolean.FALSE);
        return bean;
    }

    /**
     * Build a cache key for the given bean class and bean name.
     * 为给定的bean类和bean名称构建缓存键
     *
     * @param beanClass the bean class
     * @param beanName  the bean name
     * @return the cache key for the given class and name
     */
    protected Object getCacheKey(Class<?> beanClass, String beanName) {
        if (StringUtils.hasLength(beanName)) {
            return (GPFactoryBean.class.isAssignableFrom(beanClass) ?
                    GPBeanFactory.FACTORY_BEAN_PREFIX + beanName : beanName);
        } else {
            return beanClass;
        }
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws GPBeansException {
        Object cacheKey = getCacheKey(beanClass, beanName);

        /*if (!StringUtils.hasLength(beanName) || !this.targetSourcedBeans.contains(beanName)) {
            if (this.advisedBeans.containsKey(cacheKey)) {
                return null;
            }
            if (isInfrastructureClass(beanClass) || shouldSkip(beanClass, beanName)) {
                this.advisedBeans.put(cacheKey, Boolean.FALSE);
                return null;
            }
        }

        // Create proxy here if we have a custom TargetSource.
        // Suppresses unnecessary default instantiation of the target bean:
        // The TargetSource will handle target instances in a custom fashion.
        TargetSource targetSource = getCustomTargetSource(beanClass, beanName);
        if (targetSource != null) {
            if (StringUtils.hasLength(beanName)) {
                this.targetSourcedBeans.add(beanName);
            }
            // AOP处理：如果有advice，则创建代理
            // 获取bean匹配的advice
            Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(beanClass, beanName, targetSource);
            Object proxy = createProxy(beanClass, beanName, specificInterceptors, targetSource);
            this.proxyTypes.put(cacheKey, proxy.getClass());
            return proxy;
        }*/

        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws GPBeansException {
        return true;
    }

    /**
     * 获取代理目标对象的advices 和 advisors
     */
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, Object target) throws GPBeansException {
        // TODO

        return null;
    }
}

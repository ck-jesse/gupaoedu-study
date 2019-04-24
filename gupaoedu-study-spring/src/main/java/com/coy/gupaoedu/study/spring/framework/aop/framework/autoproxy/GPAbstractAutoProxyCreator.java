package com.coy.gupaoedu.study.spring.framework.aop.framework.autoproxy;

import com.coy.gupaoedu.study.spring.framework.aop.GPAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.GPAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.aspectj.GPAspectJAdvisorFactory;
import com.coy.gupaoedu.study.spring.framework.aop.aspectj.GPAspectJPointcutAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.framework.GPProxyFactory;
import com.coy.gupaoedu.study.spring.framework.aop.framework.GPProxyProcessorSupport;
import com.coy.gupaoedu.study.spring.framework.aop.framework.adapter.GPDefaultAdvisorAdapterRegistry;
import com.coy.gupaoedu.study.spring.framework.aop.framework.adapter.GPGlobalAdvisorAdapterRegistry;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanDefinition;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactoryAware;
import com.coy.gupaoedu.study.spring.framework.beans.GPFactoryBean;
import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeansException;
import com.coy.gupaoedu.study.spring.framework.beans.factory.config.GPInstantiationAwareBeanPostProcessor;
import com.coy.gupaoedu.study.spring.framework.core.GPOrderComparator;
import com.coy.gupaoedu.study.spring.framework.core.GPOrdered;
import com.coy.gupaoedu.study.spring.framework.core.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用AOP代理包装每个符合条件的bean的实现，在调用bean本身之前将其委托给指定的拦截器
 *
 * @author chenck
 * @date 2019/4/16 20:01
 */
public class GPAbstractAutoProxyCreator extends GPProxyProcessorSupport implements GPInstantiationAwareBeanPostProcessor, GPBeanFactoryAware, GPOrdered {


    protected final Log logger = LogFactory.getLog(GPAbstractAutoProxyCreator.class);

    private GPBeanFactory beanFactory;
    private GPBeanFactoryAdvisorRetrievalHelper beanFactoryAdvisorRetrievalHelper;

    private GPAspectJAdvisorFactory aspectJAdvisorFactory;

    /**
     * Default is global AdvisorAdapterRegistry
     */
    private GPDefaultAdvisorAdapterRegistry advisorAdapterRegistry = GPGlobalAdvisorAdapterRegistry.getInstance();
    /**
     * 早期的代理引用
     */
    private final Set<Object> earlyProxyReferences = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    private final Set<String> targetSourcedBeans = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    /**
     * 代理对象的class类型
     */
    private final Map<Object, Class<?>> proxyTypes = new ConcurrentHashMap<>(16);

    /**
     * 标记bean是否需要被代理 true表示需要被代理 false表示不需要被代理
     */
    private final Map<Object, Boolean> advisedBeans = new ConcurrentHashMap<>(256);

    @Override
    public void setBeanFactory(GPBeanFactory beanFactory) throws GPBeansException {
        this.beanFactory = beanFactory;
        // 初始化
        initBeanFactory();
    }

    /**
     * 初始化
     */
    protected void initBeanFactory() {
        // 初始化Advisor的查找工具类
        beanFactoryAdvisorRetrievalHelper = new GPBeanFactoryAdvisorRetrievalHelper(beanFactory);
        if (this.aspectJAdvisorFactory == null) {
            this.aspectJAdvisorFactory = new GPAspectJAdvisorFactory(beanFactory);
        }
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
     * 为在Bean的初始化之后提供回调入口，如果目标对象bean符合配置的拦截器，则创建一个代理
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
     * 如果给定的bean符合被代理的条件，就将其包装起来
     */
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        if (StringUtils.hasLength(beanName)) {
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
            // 创建代理
            Object proxy = createProxy(bean.getClass(), beanName, specificInterceptors, bean);
            this.proxyTypes.put(cacheKey, proxy.getClass());
            return proxy;
        }

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

        if (!StringUtils.hasLength(beanName)) {
            if (this.advisedBeans.containsKey(cacheKey)) {
                return null;
            }
            // 判断是否是代理的基础类 或者 是否应该跳过
            if (isInfrastructureClass(beanClass) || shouldSkip(beanClass, beanName)) {
                this.advisedBeans.put(cacheKey, Boolean.FALSE);
                return null;
            }
        }

        return null;
        // Create proxy here if we have a custom TargetSource.
        // Suppresses unnecessary default instantiation of the target bean:
        // The TargetSource will handle target instances in a custom fashion.
        /*
        TODO 此方法中暂时先直接返回null，让其先创建实例，然后在postProcessAfterInitialization中来对目标对象进行代理对象的创建
        if (this.beanFactory != null && this.beanFactory.containsBean(beanName)) {
            if (StringUtils.hasLength(beanName)) {
                //this.targetSourcedBeans.add(beanName);
            }
            // AOP处理：如果有advice，则创建代理
            // 获取bean匹配的advice拦截器
            Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(beanClass, beanName, null);
            Object proxy = createProxy(beanClass, beanName, specificInterceptors, null);
            this.proxyTypes.put(cacheKey, proxy.getClass());
            return proxy;
        }
        return null;*/
    }

    /**
     * 是否应该跳过（当Advisor未加载，则初始化）
     */
    protected boolean shouldSkip(Class<?> beanClass, String beanName) {
        // 查找所有符合条件的Advisor bean
        List<GPAdvisor> candidateAdvisors = findCandidateAdvisors();
        for (GPAdvisor advisor : candidateAdvisors) {
            if (advisor instanceof GPAspectJPointcutAdvisor) {
//                if (((GPAspectJPointcutAdvisor) advisor.getAdvice()).getAspectName().equals(beanName)) {
//                    return true;
//                }
            }
        }
        return false;
    }

    /**
     * Create an AOP proxy for the given bean
     * 创建指定bean的aop代理
     */
    protected Object createProxy(Class<?> beanClass, String beanName, Object[] specificInterceptors, Object target) {
        // 确定指定bean的顾问Advisor
        GPAdvisor[] advisors = buildAdvisors(beanName, specificInterceptors);

        // AOP代理工厂
        GPProxyFactory proxyFactory = new GPProxyFactory();
        proxyFactory.setProxyTargetClass(this.isProxyTargetClass());
        proxyFactory.addAdvisors(advisors);
        proxyFactory.setTarget(target);
        proxyFactory.setTargetClass(beanClass);

        if (!proxyFactory.isProxyTargetClass()) {
            // 确定指定bean是否应使用其目标类而不是其接口进行代理
            if (shouldProxyTargetClass(beanClass, beanName)) {
                // 对类进行代理，也就是使用cglib代理
                proxyFactory.setProxyTargetClass(true);
            } else {
                // 对接口进行代理，也就是使用JDK动态代理
                evaluateProxyInterfaces(beanClass, proxyFactory);
            }
        }
        return proxyFactory.getProxy(getProxyClassLoader());
    }

    /**
     * 确定指定bean是否应使用其目标类而不是其接口进行代理
     */
    protected boolean shouldProxyTargetClass(Class<?> beanClass, String beanName) {
        if (this.beanFactory.containsBeanDefinition(beanName)) {
            GPBeanDefinition bd = this.beanFactory.getBeanDefinition(beanName);
            return Boolean.TRUE.equals(bd.isProxyTargetClass());
        }
        return false;
    }

    /**
     * 确定指定bean的顾问Advisor
     */
    protected GPAdvisor[] buildAdvisors(String beanName, Object[] specificInterceptors) {
        List<Object> allInterceptors = new ArrayList<>();
        // TODO 通用拦截器处理 没看明白值是在哪里设置的？
        //GPAdvisor[] commonInterceptors = resolveInterceptorNames();
        GPAdvisor[] commonInterceptors = new GPAdvisor[0];

        if (specificInterceptors != null) {
            allInterceptors.addAll(Arrays.asList(specificInterceptors));
            if (commonInterceptors.length > 0) {
                allInterceptors.addAll(Arrays.asList(commonInterceptors));
            }
        }
        if (logger.isDebugEnabled()) {
            int nrOfCommonInterceptors = commonInterceptors.length;
            int nrOfSpecificInterceptors = (specificInterceptors != null ? specificInterceptors.length : 0);
            logger.debug("Creating implicit proxy for bean '" + beanName + "' with " + nrOfCommonInterceptors +
                    " common interceptors and " + nrOfSpecificInterceptors + " specific interceptors");
        }

        GPAdvisor[] advisors = new GPAdvisor[allInterceptors.size()];
        for (int i = 0; i < allInterceptors.size(); i++) {
            advisors[i] = this.advisorAdapterRegistry.wrap(allInterceptors.get(i));
        }
        return advisors;
    }

    /**
     * Return whether the given bean class represents an infrastructure class
     * that should never be proxied.
     * 判断是否是代理的基础结构类
     */
    protected boolean isInfrastructureClass(Class<?> beanClass) {
        boolean retVal = GPAdvice.class.isAssignableFrom(beanClass) ||
                GPPointcut.class.isAssignableFrom(beanClass) ||
                GPAdvisor.class.isAssignableFrom(beanClass);
        if (retVal) {
            logger.trace("Did not attempt to auto-proxy infrastructure class [" + beanClass.getName() + "]");
        }
        return retVal;
    }


    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws GPBeansException {
        return true;
    }

    /**
     * 获取代理目标对象的advices 和 advisors
     */
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, Object target) throws GPBeansException {
        List<GPAdvisor> advisors = findEligibleAdvisors(beanClass, beanName);
        if (advisors.isEmpty()) {
            return null;
        }
        return advisors.toArray();
    }

    /**
     * 查找合格的Advisor
     */
    protected List<GPAdvisor> findEligibleAdvisors(Class<?> beanClass, String beanName) {
        // 查找候选Advisor
        List<GPAdvisor> candidateAdvisors = findCandidateAdvisors();
        // 查找合格的Advisor
        List<GPAdvisor> eligibleAdvisors = AopUtils.findAdvisorsThatCanApply(candidateAdvisors, beanClass);
        if (!eligibleAdvisors.isEmpty()) {
            // 将合格的Advisor排序
            Collections.sort(eligibleAdvisors, GPOrderComparator.INSTANCE);
        }
        return eligibleAdvisors;
    }

    /**
     * 查找所有候选Advisor bean
     */
    protected List<GPAdvisor> findCandidateAdvisors() {
        // Add all the Spring advisors found according to superclass rules.
        List<GPAdvisor> advisors = beanFactoryAdvisorRetrievalHelper.findAdvisorBeans();
        // Build Advisors for all AspectJ aspects in the bean factory.
        if (this.aspectJAdvisorFactory != null) {
            // 构建AspectJ切面对应的Advisors
            advisors.addAll(this.aspectJAdvisorFactory.buildAspectJAdvisors());
        }
        return advisors;
    }


    @Override
    public int getOrder() {
        return 0;
    }

}

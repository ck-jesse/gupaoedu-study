package com.coy.gupaoedu.study.spring.framework.aop.framework;

import com.coy.gupaoedu.study.spring.framework.aop.GPAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.aop.GPAdvice;

/**
 * 该接口由持有AOP代理工厂配置的类实现。
 * 此配置包括Interceptors和其他advice, Advisors和proxied interfaces。
 *
 * @author chenck
 * @date 2019/4/17 11:35
 */
public interface GPAdvised {

    /**
     * 获取目标对象的Class对象
     */
    Class<?> getTargetClass();

    /**
     * Change the {@code TargetSource} used by this {@code Advised} object.
     * 设置目标对象
     */
    void setTarget(Object target);

    /**
     * Return the {@code TargetSource} used by this {@code Advised} object.
     * 获取目标对象
     */
    Object getTarget();

    /**
     * Return the interfaces proxied by the AOP proxy
     * 获取目标对象的接口集
     */
    Class<?>[] getProxiedInterfaces();

    /**
     * Determine whether the given interface is proxied.
     * @param intf the interface to check
     */
    boolean isInterfaceProxied(Class<?> intf);

    /**
     * Return the advisors applying to this proxy
     */
    GPAdvisor[] getAdvisors();

    /**
     * Add an advisor at the end of the advisor chain
     */
    void addAdvisor(GPAdvisor advisor);

    /**
     * Add an Advisor at the specified position in the chain
     */
    void addAdvisor(int pos, GPAdvisor advisor);

    /**
     * Remove the given advisor.
     */
    boolean removeAdvisor(GPAdvisor advisor);

    /**
     * Remove the advisor at the given index.
     *
     * @param index index of advisor to remove
     */
    void removeAdvisor(int index);

    /**
     * Return the index (from 0) of the given advisor,
     * or -1 if no such advisor applies to this proxy.
     */
    int indexOf(GPAdvisor advisor);

    /**
     * Add the given AOP Alliance advice to the tail of the advice (interceptor) chain.
     */
    void addAdvice(GPAdvice advice) throws AopConfigException;

    /**
     * Add the given AOP Alliance Advice at the specified position in the advice chain.
     */
    void addAdvice(int pos, GPAdvice advice) throws AopConfigException;

    /**
     * Remove the Advisor containing the given advice.
     *
     * @param advice the advice to remove
     * @return {@code true} of the advice was found and removed;
     * {@code false} if there was no such advice
     */
    boolean removeAdvice(GPAdvice advice);

    /**
     * Return the index (from 0) of the given AOP Alliance Advice,
     */
    int indexOf(GPAdvice advice);
}

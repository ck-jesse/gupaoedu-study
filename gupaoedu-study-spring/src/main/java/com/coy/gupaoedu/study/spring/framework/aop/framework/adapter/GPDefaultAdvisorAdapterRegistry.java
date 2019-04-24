package com.coy.gupaoedu.study.spring.framework.aop.framework.adapter;

import com.coy.gupaoedu.study.spring.framework.aop.GPAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.support.GPDefaultPointcutAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.GPAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInterceptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Advisor 适配器注册工具
 *
 * @author chenck
 * @date 2019/4/17 14:32
 */
public class GPDefaultAdvisorAdapterRegistry implements Serializable {

    /**
     * Advisor 适配器集合
     */
    private final List<GPAdvisorAdapter> adapters = new ArrayList<>(3);

    /**
     * Create a new DefaultAdvisorAdapterRegistry, registering well-known adapters.
     */
    public GPDefaultAdvisorAdapterRegistry() {
        registerAdvisorAdapter(new GPMethodBeforeAdviceAdapter());
        registerAdvisorAdapter(new GPAfterReturningAdviceAdapter());
        registerAdvisorAdapter(new GPThrowsAdviceAdapter());
    }

    /**
     * 将Advice通知包装为Advisor顾问
     */
    public GPAdvisor wrap(Object adviceObject) throws UnknownAdviceTypeException {
        if (adviceObject instanceof GPAdvisor) {
            return (GPAdvisor) adviceObject;
        }
        if (!(adviceObject instanceof GPAdvice)) {
            throw new UnknownAdviceTypeException(adviceObject);
        }
        GPAdvice advice = (GPAdvice) adviceObject;
        if (advice instanceof GPMethodInterceptor) {
            // So well-known it doesn't even need an adapter.
            // 如果是
            return new GPDefaultPointcutAdvisor(advice);
        }
        for (GPAdvisorAdapter adapter : this.adapters) {
            // Check that it is supported.
            //
            if (adapter.supportsAdvice(advice)) {
                return new GPDefaultPointcutAdvisor(advice);
            }
        }
        throw new UnknownAdviceTypeException(advice);
    }

    /**
     * 获取拦截器集合
     */
    public GPMethodInterceptor[] getInterceptors(GPAdvisor advisor) throws UnknownAdviceTypeException {
        List<GPMethodInterceptor> interceptors = new ArrayList<>(3);
        GPAdvice advice = advisor.getAdvice();
        if (advice instanceof GPMethodInterceptor) {
            interceptors.add((GPMethodInterceptor) advice);
        }
        for (GPAdvisorAdapter adapter : this.adapters) {
            if (adapter.supportsAdvice(advice)) {
                interceptors.add(adapter.getInterceptor(advisor));
            }
        }
        if (interceptors.isEmpty()) {
            throw new UnknownAdviceTypeException(advisor.getAdvice());
        }
        return interceptors.toArray(new GPMethodInterceptor[interceptors.size()]);
    }

    /**
     * 注册Advisor适配器
     */
    public void registerAdvisorAdapter(GPAdvisorAdapter adapter) {
        this.adapters.add(adapter);
    }
}

package com.coy.gupaoedu.study.spring.framework.aop.framework;

import com.coy.gupaoedu.study.spring.framework.aop.GPAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.GPPointcutAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.GPAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPInterceptor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInterceptor;
import com.coy.gupaoedu.study.spring.framework.aop.framework.adapter.GPDefaultAdvisorAdapterRegistry;
import com.coy.gupaoedu.study.spring.framework.aop.framework.adapter.GPGlobalAdvisorAdapterRegistry;
import com.coy.gupaoedu.study.spring.framework.aop.framework.adapter.UnknownAdviceTypeException;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPMethodMatcher;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Advisor 顾问链工厂
 * 为一种方法制定建议链的简单但明确的方法
 *
 * @author chenck
 * @date 2019/4/17 13:59
 */
public class GPDefaultAdvisorChainFactory implements Serializable {

    /**
     * 获取拦截器链
     */
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(
            GPAdvised config, Method method, Class<?> targetClass) {

        // 拦截器列表（要保证元素的有序性）
        List<Object> interceptorList = new ArrayList<>(config.getAdvisors().length);
        // 目标对象的class
        Class<?> actualClass = (targetClass != null ? targetClass : method.getDeclaringClass());
        // Advisor 适配器注册工具
        GPDefaultAdvisorAdapterRegistry registry = GPGlobalAdvisorAdapterRegistry.getInstance();

        for (GPAdvisor advisor : config.getAdvisors()) {
            if (advisor instanceof GPPointcutAdvisor) {
                // Add it conditionally.
                GPPointcutAdvisor pointcutAdvisor = (GPPointcutAdvisor) advisor;
                // 类是否匹配该切入点(不同的ClassFilter可实现不同的匹配规则)
                if (pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)) {
                    // 此处去掉，直接从advisor中获取advice即可
                    //GPMethodInterceptor[] interceptors = registry.getInterceptors(advisor);

                    GPMethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
                    // 方法是否匹配该切入点(不同的MethodMatcher可实现不同的匹配规则)
                    if (mm.matches(method, actualClass)) {

                        // 判断是否为MethodInterceptor
                        GPAdvice advice = advisor.getAdvice();
                        if (!(advice instanceof GPMethodInterceptor)) {
                            throw new UnknownAdviceTypeException(advice);
                        }
                        if (mm.isRuntime()) {
                            // Creating a new object instance in the getInterceptors() method
                            // isn't a problem as we normally cache created chains.
                            interceptorList.add(new GPInterceptorAndDynamicMethodMatcher((GPMethodInterceptor) advice, mm));
                        } else {
                            interceptorList.add(advice);
                        }
                    }
                }
            } else {
                GPInterceptor[] interceptors = registry.getInterceptors(advisor);
                interceptorList.addAll(Arrays.asList(interceptors));
            }
        }
        return interceptorList;
    }
}

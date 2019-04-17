package com.coy.gupaoedu.study.spring.framework.aop.framework;

import com.coy.gupaoedu.study.spring.framework.aop.GPMethodMatcher;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInterceptor;

/**
 * 框架的内部类，将methodInterceptor实例与methodMatcher组合，用作Advisor链中的元素
 *
 * @autr chenck
 * @date 2019/4/17 17:51
 */
class GPInterceptorAndDynamicMethodMatcher {
    final GPMethodInterceptor interceptor;

    final GPMethodMatcher methodMatcher;

    public GPInterceptorAndDynamicMethodMatcher(GPMethodInterceptor interceptor, GPMethodMatcher methodMatcher) {
        this.interceptor = interceptor;
        this.methodMatcher = methodMatcher;
    }

}

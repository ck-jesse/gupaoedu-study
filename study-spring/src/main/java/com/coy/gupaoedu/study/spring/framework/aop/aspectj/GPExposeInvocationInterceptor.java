/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.GPAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInterceptor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInvocation;
import com.coy.gupaoedu.study.spring.framework.aop.support.GPDefaultPointcutAdvisor;
import com.coy.gupaoedu.study.spring.framework.core.GPPriorityOrdered;
import com.coy.gupaoedu.study.spring.framework.core.NamedThreadLocal;

import java.io.Serializable;

/**
 * 将当前拦截器的MethodInvocation设置到线程副本ThreadLocal中
 * <p>
 * 除非确实需要，否则不要使用此拦截器
 * <p>
 * 如果使用，这个拦截器通常是拦截器链中的第一个
 *
 * 思考：
 * 此InvocationInterceptor的定义挺有意思的，后续对于这种拦截器链的不同切入方法的参数混乱的问题，可以借鉴这种思路来实现。
 * 前提：methodA方法 和 methodB方法都被同一个后置拦截器给拦截
 * 问题：methodA方法 调用 methodB方法时， methodB方法执行完以后，会导致该后置拦截器中的Invocation属性从InvocationA被替换为了InvocationB，
 * 而在执行权限回到methodA方法时，取到的就是InvocationB了，所以出现了安全性的问题。
 */
public class GPExposeInvocationInterceptor implements GPMethodInterceptor, GPPriorityOrdered, Serializable {

    /**
     * Singleton instance of this class
     */
    public static final GPExposeInvocationInterceptor INSTANCE = new GPExposeInvocationInterceptor();

    /**
     * Singleton advisor for this class. Use in preference to INSTANCE when using
     * Spring AOP, as it prevents the need to create a new Advisor to wrap the instance.
     */
    public static final GPAdvisor ADVISOR = new GPDefaultPointcutAdvisor(INSTANCE) {
        @Override
        public String toString() {
            return GPExposeInvocationInterceptor.class.getName() + ".ADVISOR";
        }
    };

    private static final ThreadLocal<GPMethodInvocation> invocation = new NamedThreadLocal<>("Current AOP method invocation");


    /**
     * Return the AOP Alliance MethodInvocation object associated with the current invocation.
     *
     * @return the invocation object associated with the current invocation
     * @throws IllegalStateException if there is no AOP invocation in progress,
     *                               or if the ExposeInvocationInterceptor was not added to this interceptor chain
     */
    public static GPMethodInvocation currentInvocation() throws IllegalStateException {
        GPMethodInvocation mi = invocation.get();
        if (mi == null)
            throw new IllegalStateException(
                    "No MethodInvocation found: Check that an AOP invocation is in progress, and that the " +
                            "ExposeInvocationInterceptor is upfront in the interceptor chain. Specifically, note that " +
                            "advices with order HIGHEST_PRECEDENCE will execute before ExposeInvocationInterceptor!");
        return mi;
    }


    /**
     * Ensures that only the canonical instance can be created.
     */
    private GPExposeInvocationInterceptor() {
    }

    /**
     * 保证每一个拦截器获取到的invocation都是正确的
     */
    @Override
    public Object invoke(GPMethodInvocation mi) throws Throwable {
        // 从ThreadLocal中将旧的Invocation取出来，并记录下来
        // methodA方法 调用 methodB方法时
        // methodA方法进来时oldInvocation=null
        // methodB方法进来时oldInvocation=methodAInvocation，这样保证methodB方法执行完后，methodA方法执行时获得自己的Invocation
        // 这样保证嵌套方法都被拦截时外层方法获取的Invocation是正确的。
        GPMethodInvocation oldInvocation = invocation.get();
        // 设置当前Invocation到ThreadLocal中，保证当前的拦截器能获取到自己的Invocation
        invocation.set(mi);
        try {
            return mi.proceed();
        } finally {
            // 将旧的Invocation设置到ThreadLocal中，保证外层的拦截器能获取到自己的Invocation
            invocation.set(oldInvocation);
        }
    }

    /**
     *
     */
    @Override
    public int getOrder() {
        return GPPriorityOrdered.HIGHEST_PRECEDENCE + 1;
    }

    /**
     * Required to support serialization. Replaces with canonical instance
     * on deserialization, protecting Singleton pattern.
     * <p>Alternative to overriding the {@code equals} method.
     */
    private Object readResolve() {
        return INSTANCE;
    }

}

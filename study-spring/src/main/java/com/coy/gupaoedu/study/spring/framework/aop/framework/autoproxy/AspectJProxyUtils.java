/*
 * Copyright 2002-2012 the original author or authors.
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

package com.coy.gupaoedu.study.spring.framework.aop.framework.autoproxy;

import com.coy.gupaoedu.study.spring.framework.aop.GPAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.GPPointcutAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.aspectj.GPAbstractAspectJAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.aspectj.GPAspectJExpressionPointcut;
import com.coy.gupaoedu.study.spring.framework.aop.aspectj.GPExposeInvocationInterceptor;

import java.util.List;

/**
 * Utility methods for working with AspectJ proxies.
 *
 * @author Rod Johnson
 * @author Ramnivas Laddad
 * @since 2.0
 */
public abstract class AspectJProxyUtils {

    /**
     * 如果需要，则添加特殊顾问advisors到AspectJ顾问的代理链中，使其能够在代理链中工作
     *
     * Add special advisors if necessary to work with a proxy chain that contains AspectJ advisors.
     * This will expose the current Spring AOP invocation (necessary for some AspectJ pointcut matching)
     * and make available the current AspectJ JoinPoint. The call will have no effect if there are no
     * AspectJ advisors in the advisor chain.
     *
     * @param advisors Advisors available
     */
    public static boolean makeAdvisorChainAspectJCapableIfNecessary(List<GPAdvisor> advisors) {
        // Don't add advisors to an empty list; may indicate that proxying is just not required
        if (!advisors.isEmpty()) {
            boolean foundAspectJAdvice = false;
            for (GPAdvisor advisor : advisors) {
                // Be careful not to get the Advice without a guard, as
                // this might eagerly instantiate a non-singleton AspectJ aspect
                if (isAspectJAdvice(advisor)) {
                    foundAspectJAdvice = true;
                }
            }
            if (foundAspectJAdvice && !advisors.contains(GPExposeInvocationInterceptor.ADVISOR)) {
                // 添加到拦截器链的第一个元素
                advisors.add(0, GPExposeInvocationInterceptor.ADVISOR);
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether the given Advisor contains an AspectJ advice.
     *
     * @param advisor the Advisor to check
     */
    private static boolean isAspectJAdvice(GPAdvisor advisor) {
        return (advisor.getAdvice() instanceof GPAbstractAspectJAdvice ||
                (advisor instanceof GPPointcutAdvisor && ((GPPointcutAdvisor) advisor).getPointcut() instanceof GPAspectJExpressionPointcut));
    }

}

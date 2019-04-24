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

package com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept;

import com.coy.gupaoedu.study.spring.framework.aop.advice.GPMethodBeforeAdvice;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;

import java.io.Serializable;

/**
 * Interceptor to wrap am {@link org.springframework.aop.MethodBeforeAdvice}.
 * Used internally by the AOP framework; application developers should not need
 * to use this class directly.
 *
 * @author Rod Johnson
 */
@SuppressWarnings("serial")
public class GPMethodBeforeAdviceInterceptor implements GPMethodInterceptor, Serializable {

    private GPMethodBeforeAdvice advice;


    /**
     * Create a new MethodBeforeAdviceInterceptor for the given advice.
     *
     * @param advice the MethodBeforeAdvice to wrap
     */
    public GPMethodBeforeAdviceInterceptor(GPMethodBeforeAdvice advice) {
        Assert.notNull(advice, "Advice must not be null");
        this.advice = advice;
    }

    @Override
    public Object invoke(GPMethodInvocation mi) throws Throwable {
        this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }

}

/*
 * Copyright 2002-2017 the original author or authors.
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

import com.coy.gupaoedu.study.spring.framework.aop.advice.GPAfterAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.advice.GPAfterReturningAdvice;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;

import java.io.Serializable;

/**
 * Interceptor to wrap an {@link org.springframework.aop.AfterReturningAdvice}.
 * Used internally by the AOP framework; application developers should not need
 * to use this class directly.
 *
 * @author Rod Johnson
 */
public class GPAfterReturningAdviceInterceptor implements GPMethodInterceptor, GPAfterAdvice, Serializable {

    private final GPAfterReturningAdvice advice;

    /**
     * Create a new AfterReturningAdviceInterceptor for the given advice.
     *
     * @param advice the AfterReturningAdvice to wrap
     */
    public GPAfterReturningAdviceInterceptor(GPAfterReturningAdvice advice) {
        Assert.notNull(advice, "Advice must not be null");
        this.advice = advice;
    }

    @Override
    public Object invoke(GPMethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }

}

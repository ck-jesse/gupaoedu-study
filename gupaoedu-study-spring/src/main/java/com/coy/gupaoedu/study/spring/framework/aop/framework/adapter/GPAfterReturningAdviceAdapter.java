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

package com.coy.gupaoedu.study.spring.framework.aop.framework.adapter;

import com.coy.gupaoedu.study.spring.framework.aop.GPAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.advice.GPAfterReturningAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.GPAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPAfterReturningAdviceInterceptor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInterceptor;

import java.io.Serializable;

/**
 * Adapter to enable {@link org.springframework.aop.AfterReturningAdvice}
 * to be used in the Spring AOP framework.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 */
@SuppressWarnings("serial")
class GPAfterReturningAdviceAdapter implements GPAdvisorAdapter, Serializable {

	@Override
	public boolean supportsAdvice(GPAdvice advice) {
		return (advice instanceof GPAfterReturningAdvice);
	}

	@Override
	public GPMethodInterceptor getInterceptor(GPAdvisor advisor) {
		GPAfterReturningAdvice advice = (GPAfterReturningAdvice) advisor.getAdvice();
		return new GPAfterReturningAdviceInterceptor(advice);
	}

}

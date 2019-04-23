package com.coy.gupaoedu.study.spring.framework.aop;

import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;

/**
 * @author chenck
 * @date 2019/4/23 17:03
 */
public interface GPExpressionPointcut extends GPPointcut {

    /**
     * Return the String expression for this pointcut.
     */
    String getExpression();
}

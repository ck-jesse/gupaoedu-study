package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPClassFilter;
import com.coy.gupaoedu.study.spring.framework.aop.GPExpressionPointcut;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPMethodMatcher;

/**
 * @author chenck
 * @date 2019/4/23 17:03
 */
public class GPAspectJExpressionPointcut implements GPExpressionPointcut {


    @Override
    public String getExpression() {
        return null;
    }

    @Override
    public GPClassFilter getClassFilter() {
        return null;
    }

    @Override
    public GPMethodMatcher getMethodMatcher() {
        return null;
    }

}

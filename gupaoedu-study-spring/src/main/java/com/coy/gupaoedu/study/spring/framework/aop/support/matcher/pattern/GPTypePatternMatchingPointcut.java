package com.coy.gupaoedu.study.spring.framework.aop.support.matcher.pattern;

import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPClassFilter;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPMethodMatcher;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;

/**
 * 正则表达式的切入点
 *
 * @author chenck
 * @date 2019/4/23 20:57
 */
public class GPTypePatternMatchingPointcut implements GPPointcut {

    private final GPClassFilter classFilter;

    private final GPMethodMatcher methodMatcher;

    public GPTypePatternMatchingPointcut(String typePattern) {
        if (typePattern != null) {
            this.classFilter = new GPTypePatternClassFilter(typePattern);
        } else {
            this.classFilter = GPClassFilter.TRUE;
        }

        if (typePattern != null) {
            this.methodMatcher = new GPTypePatternMethodMatcher(typePattern);
        } else {
            this.methodMatcher = GPMethodMatcher.TRUE;
        }
    }

    @Override
    public GPClassFilter getClassFilter() {
        return this.classFilter;
    }

    @Override
    public GPMethodMatcher getMethodMatcher() {
        return this.methodMatcher;
    }
}

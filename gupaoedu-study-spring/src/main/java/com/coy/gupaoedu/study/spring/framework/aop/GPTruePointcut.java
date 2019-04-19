package com.coy.gupaoedu.study.spring.framework.aop;

import java.io.Serializable;

/**
 * 匹配的规范切入点实例
 *
 * @author chenck
 * @date 2019/4/17 13:01
 */
public class GPTruePointcut implements GPPointcut, Serializable {

    public static final GPTruePointcut INSTANCE = new GPTruePointcut();

    /**
     * Enforce Singleton pattern.
     */
    private GPTruePointcut() {
    }

    @Override
    public GPClassFilter getClassFilter() {
        return GPClassFilter.TRUE;
    }

    @Override
    public GPMethodMatcher getMethodMatcher() {
        return GPMethodMatcher.TRUE;
    }

    /**
     * Required to support serialization. Replaces with canonical
     * instance on deserialization, protecting Singleton pattern.
     * Alternative to overriding {@code equals()}.
     */
    private Object readResolve() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "Pointcut.TRUE";
    }
}

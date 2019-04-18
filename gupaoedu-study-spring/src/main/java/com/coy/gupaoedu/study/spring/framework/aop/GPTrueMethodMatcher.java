package com.coy.gupaoedu.study.spring.framework.aop;

import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 表示匹配所有的规则
 *
 * @author chenck
 * @date 2019/4/17 13:02
 */
public class GPTrueMethodMatcher implements GPMethodMatcher, Serializable {

    public static final GPTrueMethodMatcher INSTANCE = new GPTrueMethodMatcher();


    /**
     * Enforce Singleton pattern.
     */
    private GPTrueMethodMatcher() {
    }


    @Override
    public boolean isRuntime() {
        return false;
    }

    @Override
    public boolean matches(Method method,  Class<?> targetClass) {
        return true;
    }

    @Override
    public boolean matches(Method method,  Class<?> targetClass, Object... args) {
        // Should never be invoked as isRuntime returns false.
        throw new UnsupportedOperationException();
    }


    @Override
    public String toString() {
        return "MethodMatcher.TRUE";
    }

    /**
     * Required to support serialization. Replaces with canonical
     * instance on deserialization, protecting Singleton pattern.
     * Alternative to overriding {@code equals()}.
     */
    private Object readResolve() {
        return INSTANCE;
    }
}

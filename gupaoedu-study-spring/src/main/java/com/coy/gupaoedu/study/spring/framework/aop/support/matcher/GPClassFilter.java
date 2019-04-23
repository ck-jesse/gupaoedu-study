package com.coy.gupaoedu.study.spring.framework.aop.support.matcher;

/**
 * 类切入点匹配的筛选器
 *
 * @author chenck
 * @date 2019/4/17 17:45
 */
public interface GPClassFilter {

    /**
     * 切入点是否应应用于给定的接口或目标类
     */
    boolean matches(Class<?> clazz);


    /**
     * Canonical instance of a ClassFilter that matches all classes.
     */
    GPClassFilter TRUE = GPTrueClassFilter.INSTANCE;

}

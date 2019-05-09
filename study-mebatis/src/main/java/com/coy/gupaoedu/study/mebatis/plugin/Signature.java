package com.coy.gupaoedu.study.mebatis.plugin;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 拦截方法的签名
 *
 * @author chenck
 * @date 2019/5/9 13:37
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Signature {
    /**
     * 拦截的类
     */
    Class<?> type();

    /**
     * 拦截的方法
     */
    String method();

    /**
     * 拦截方法的参数类型
     */
    Class<?>[] args();
}
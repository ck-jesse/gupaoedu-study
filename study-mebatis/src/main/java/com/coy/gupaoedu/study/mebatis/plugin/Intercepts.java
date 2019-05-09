package com.coy.gupaoedu.study.mebatis.plugin;

import java.lang.annotation.*;

/**
 * 用于注解拦截器，指定拦截的方法
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Intercepts {
    /**
     * 拦截方法的签名
     */
    Signature[] value();
}
package com.coy.gupaoedu.study.spring.framework.context.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@GPComponent
public @interface GPService {
    String value() default "";
}

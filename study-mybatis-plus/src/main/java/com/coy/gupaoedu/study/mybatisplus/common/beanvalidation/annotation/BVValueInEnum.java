package com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation;

import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.validator.ValueInEnumValidator;
import org.hibernate.validator.constraints.Length;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 包含约束注解（验证参数的合法性）<br>
 * 用于判断参数值是否在指定的值里面<br>
 * <p>
 * 将枚举类作为字典来源
 *
 * @author chenck
 * @date 2019/9/10 11:59
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ValueInEnumValidator.class})
public @interface BVValueInEnum {

    /**
     * 枚举列表
     */
    Class<?>[] enums() default {};

    String message() default "Parameter is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@code @Length} annotations on the same element.
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        Length[] value();
    }

}

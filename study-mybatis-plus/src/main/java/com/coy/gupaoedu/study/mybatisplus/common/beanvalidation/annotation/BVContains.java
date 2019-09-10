package com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation;

import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.validator.ContainValidator;
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
 *
 * @author chenck
 * @date 2019/9/10 11:59
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ContainValidator.class})
public @interface BVContains {

    /**
     * 来源数据类型，枚举，字典，字符串，数值等
     */
    Type type() default Type.CHAR_SEQUENCE;

    /**
     * 枚举列表
     */
    Class<?>[] enums() default {};

    /**
     * 字典列表[字符串]
     */
    String[] charDicts() default {};

    /**
     * 字典列表[数值]
     */
    int[] numberDicts() default {};

    /**
     * 字典key,缓存或DB中获取来源数据进行验证<br>
     */
    String dictKey() default "";

    String message() default "Parameter is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 来源数据类型
     */
    public static enum Type {
        /**
         * 枚举类型：表示从枚举类中获取所有值与参数值进行合法性验证
         */
        ENUM,
        /**
         * 字符串类型：表示直接通过@Contain的dictList属性中获取来源数据进行合法性验证
         */
        CHAR_SEQUENCE,
        /**
         * 数值类型：表示直接通过@Contain的dictList属性中获取来源数据进行合法性验证
         */
        NUMBER,
        /**
         * 字典类型：表示从缓存或DB中获取字典数据进行合法性验证
         * TODO 因该场景比较不可控，所以暂不实现
         */
        DICT,
    }


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

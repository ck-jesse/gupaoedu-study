package com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation;

import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.validator.LengthForCharSequenceValidator;
import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.validator.LengthForNumberValidator;
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
 * 扩展Hibernate @Length 支持Number和CharSequence类型长度校验<br>
 * 注：基于JSR-303规范来进行扩展（也就是Bean Validation）
 *
 * @author chenck
 * @date 2019/9/10 11:45
 */
@Documented
@Constraint(validatedBy = {LengthForNumberValidator.class, LengthForCharSequenceValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
public @interface BVLength {
    int min() default 0;

    int max() default Integer.MAX_VALUE;

    String message() default "";

    /**
     * 该属性针对类型为CharSequence的字段进行长度校验<br>
     * 1.默认为空，表示根据str.length()取字符串的长度校验<br>
     * 2.具体字符集，表示根据str.getBytes("charsetName").length取字符串的字节长度校验<br>
     * GBK和GB2312编码:一个英文字母字符存储需要1个字节，一个汉字字符存储需要2个字节<br>
     * UTF-8编码:一个英文字母字符存储需要1个字节，一个汉字字符储存需要3到4个字节。<br>
     * UTF-16编码:一个英文字母字符或一个汉字字符存储 都需要2个字节（Unicode扩展区的一些汉字存储需要4个字节）。<br>
     * UTF-32编码:世界上任何字符的存储都需要4个字节。<br>
     */
    String charsetName() default "";

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

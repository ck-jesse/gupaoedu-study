package com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.validator;


import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation.BVLength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.UnsupportedEncodingException;

/**
 * 扩展Hibernate @Length 支持CharSequence类型长度校验<br>
 *
 * @author chenck
 * @date 2019/9/10 11:46
 */
public class LengthForCharSequenceValidator implements ConstraintValidator<BVLength, CharSequence> {

    private int min;
    private int max;
    private String charsetName;

    @Override
    public void initialize(BVLength parameters) {
        min = parameters.min();
        max = parameters.max();
        charsetName = parameters.charsetName();
        validateParameters();
    }

    @Override
    public boolean isValid(CharSequence value,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        int length = value.length();
        // 字符集不为空，则根据该字符集取字符串的字节长度
        if (null != charsetName && !"".equals(charsetName.trim())) {
            try {
                length = value.toString().getBytes(charsetName).length;
            } catch (UnsupportedEncodingException e) {
                throw new IllegalArgumentException("The charsetName parameter unsupported.");
            }
        }
        return length >= min && length <= max;
    }

    private void validateParameters() {
        if (min < 0) {
            throw new IllegalArgumentException("The min parameter cannot be negative.");
        }
        if (max < 0) {
            throw new IllegalArgumentException("The max parameter cannot be negative.");
        }
        if (max < min) {
            throw new IllegalArgumentException("The length cannot be negative.");
        }
    }
}

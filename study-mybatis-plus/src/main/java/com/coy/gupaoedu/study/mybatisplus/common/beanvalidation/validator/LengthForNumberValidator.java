package com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.validator;

import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation.BVLength;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 扩展Hibernate @Length 支持Number类型长度校验<br>
 *
 * @author chenck
 * @date 2019/9/10 11:46
 */
public class LengthForNumberValidator implements ConstraintValidator<BVLength, Number> {

    private int min;
    private int max;

    @Override
    public void initialize(BVLength parameters) {
        min = parameters.min();
        max = parameters.max();
        validateParameters();
    }

    @Override
    public boolean isValid(Number value,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }
        int length = String.valueOf(value).length();
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

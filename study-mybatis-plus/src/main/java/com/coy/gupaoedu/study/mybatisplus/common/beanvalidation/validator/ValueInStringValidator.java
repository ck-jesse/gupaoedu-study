package com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.validator;

import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation.BVValueInString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * 包含约束注解 @BVValueInString 验证器
 *
 * @author chenck
 * @date 2019/9/10 12:12
 */
public class ValueInStringValidator implements ConstraintValidator<BVValueInString, CharSequence> {

    private String[] dicts;

    @Override
    public void initialize(BVValueInString contain) {
        this.dicts = contain.dicts();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        // 允许为空
        if (null == value) {
            return true;
        }
        if (value instanceof CharSequence && "".equals(value.toString())) {
            return true;
        }
        return validCharSequence(value);
    }

    private boolean validCharSequence(CharSequence value) {
        if (null == dicts || dicts.length == 0) {
            throw new IllegalArgumentException("@BVValueInString.dicts() attribute is empty");
        }
        String v = "";
        try {
            v = value.toString();
        } catch (Exception e) {
            return false;
        }
        for (String dict : dicts) {
            if (dict.equals(v)) {
                return true;
            }
        }
        return false;
    }

}

package com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.validator;

import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation.BVValueInNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * 包含约束注解 @BVValueInNumber 验证器
 *
 * @author chenck
 * @date 2019/9/10 12:12
 */
public class ValueInNumberValidator implements ConstraintValidator<BVValueInNumber, Number> {

    private int[] dicts;

    @Override
    public void initialize(BVValueInNumber contain) {
        this.dicts = contain.dicts();
    }

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        // 允许为空
        if (null == value) {
            return true;
        }
        return validNumber(value);

    }

    private boolean validNumber(Number value) {
        if (null == dicts || dicts.length == 0) {
            throw new IllegalArgumentException("@BVValueInNumber.dicts() attribute is empty");
        }
        int v = 0;
        try {
            v = Integer.valueOf(value.toString());
        } catch (Exception e) {
            return false;
        }
        for (int dict : dicts) {
            if (dict == v) {
                return true;
            }
        }
        return false;
    }

}

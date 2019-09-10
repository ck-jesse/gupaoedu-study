package com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.validator;

import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.BVBaseEnum;
import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation.BVValueInEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * 包含约束注解 @BVValueInEnum 验证器
 *
 * @author chenck
 * @date 2019/9/10 12:12
 */
public class ValueInEnumValidator implements ConstraintValidator<BVValueInEnum, Object> {

    private Class<?>[] enums;

    @Override
    public void initialize(BVValueInEnum contain) {
        this.enums = contain.enums();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // 允许为空
        if (null == value) {
            return true;
        }
        if (value instanceof CharSequence && "".equals(value.toString())) {
            return true;
        }
        return validEnum(value);
    }

    private boolean validEnum(Object value) {
        if (enums.length == 0) {
            throw new IllegalArgumentException("@BVValueInEnum.enums() attribute is empty");
        }
        boolean flag = false;
        for (Class<?> clazz : enums) {
            flag = false;
            // 校验是否是枚举
            if (!clazz.isEnum()) {
                throw new IllegalArgumentException("@BVValueInEnum.enums() is not enum");
            }
            // 校验枚举是否实现IBeanValid接口
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> clazz1 : interfaces) {
                if (clazz1.getName().equals(BVBaseEnum.class.getName())) {
                    flag = true;
                }
            }
            if (!flag) {
                throw new IllegalArgumentException(
                        clazz.getName() + " enum must be implements " + BVBaseEnum.class.getName());
            }

            // 将clazz对象转换为指定具体类型IBeanValid的子类,返回Class对象
            Class<? extends BVBaseEnum> baseEnum = clazz.asSubclass(BVBaseEnum.class);
            BVBaseEnum[] baseEnumList = baseEnum.getEnumConstants();
            for (BVBaseEnum enumObj : baseEnumList) {
                // 判断枚举本身
                if (value.equals(enumObj)) {
                    return true;
                }
                // 判断枚举值
                if (value.equals(enumObj.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

}

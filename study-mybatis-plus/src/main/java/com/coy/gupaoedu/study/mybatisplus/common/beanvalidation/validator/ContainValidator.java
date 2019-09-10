package com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.validator;

import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.BVBaseEnum;
import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation.BVContains;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * 包含约束注解@Contains验证器
 *
 * @author chenck
 * @date 2019/9/10 12:12
 */
public class ContainValidator implements ConstraintValidator<BVContains, Object> {

    private BVContains.Type type;
    private Class<?>[] enums;
    private String dictKey;
    private String[] charDicts;
    private int[] numberDicts;

    @Override
    public void initialize(BVContains contain) {
        this.type = contain.type();
        this.enums = contain.enums();
        this.dictKey = contain.dictKey();
        this.charDicts = contain.charDicts();
        this.numberDicts = contain.numberDicts();
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

        if (type == BVContains.Type.ENUM) {
            return validEnum(value);
        } else if (type == BVContains.Type.DICT) {
            return validDict(value);
        } else if (type == BVContains.Type.CHAR_SEQUENCE) {
            return validCharSequence(value);
        } else if (type == BVContains.Type.NUMBER) {
            return validNumber(value);
        } else {
            throw new IllegalArgumentException("@Contain.type() attribute is not valid");
        }
    }

    private boolean validEnum(Object value) {
        if (enums.length == 0) {
            throw new IllegalArgumentException("@Contain.enums() attribute is empty");
        }
        boolean flag = false;
        for (Class<?> clazz : enums) {
            flag = false;
            // 校验是否是枚举
            if (!clazz.isEnum()) {
                throw new IllegalArgumentException("@Contain.enums() is not enum");
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

    private boolean validDict(Object value) {
        if (null == dictKey || "".equals(dictKey.trim())) {
            throw new IllegalArgumentException("@Contain.dictKey() attribute is empty");
        }
        // TODO 根据dictKey从缓存或DB中获取来源数据进行验证
        return true;
    }

    private boolean validCharSequence(Object value) {
        if (null == charDicts || charDicts.length == 0) {
            throw new IllegalArgumentException("@Contain.charDicts() attribute is empty");
        }
        String v = "";
        try {
            v = value.toString();
        } catch (Exception e) {
            return false;
        }
        for (String dict : charDicts) {
            if (dict.equals(v)) {
                return true;
            }
        }
        return false;
    }

    private boolean validNumber(Object value) {
        if (null == numberDicts || numberDicts.length == 0) {
            throw new IllegalArgumentException("@Contain.intDicts() attribute is empty");
        }
        int v = 0;
        try {
            v = Integer.valueOf(value.toString());
        } catch (Exception e) {
            return false;
        }
        for (int dict : numberDicts) {
            if (dict == v) {
                return true;
            }
        }
        return false;
    }

}

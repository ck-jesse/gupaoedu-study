package com.coy.gupaoedu.study.mybatisplus.enums;


import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.BVBaseEnum;

public enum SexEnum implements BVBaseEnum {
    MAN(0), FEMALE(1),;

    private int value;

    private SexEnum(int value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        // TODO Auto-generated method stub
        return value;
    }

}

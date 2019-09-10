package com.coy.gupaoedu.study.mybatisplus.enums;


import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.BVBaseEnum;

public enum ResultEnum implements BVBaseEnum {

    SUCC("succ"), FAIL("fail"),
    ;

    private String value;

    private ResultEnum(String value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        // TODO Auto-generated method stub
        return value;
    }
}

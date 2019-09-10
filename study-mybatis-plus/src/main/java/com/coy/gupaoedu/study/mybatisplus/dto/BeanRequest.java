package com.coy.gupaoedu.study.mybatisplus.dto;

import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation.BVValueInEnum;
import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation.BVValueInNumber;
import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation.BVValueInString;
import com.coy.gupaoedu.study.mybatisplus.enums.SexEnum;
import lombok.Data;

/**
 * @author chenck
 * @date 2019/9/10 18:49
 */
@Data
public class BeanRequest {

    // 自定义约束注解
    @BVValueInEnum(enums = SexEnum.class, message = "ValueInEnum 性别不合法")
    private int sexEnum;

    @BVValueInNumber(dicts = {0, 1}, message = "BVValueInNumber 性别不合法")
    private int sexNumber;

    @BVValueInString(dicts = {"0", "1"}, message = "BVValueInString 性别不合法")
    private String sexString;
}

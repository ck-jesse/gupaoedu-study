package com.coy.gupaoedu.study.mybatisplus.controller;

import com.coy.gupaoedu.study.mybatisplus.common.domain.DataResponse;
import com.coy.gupaoedu.study.mybatisplus.controller.group.GroupA;
import com.coy.gupaoedu.study.mybatisplus.controller.group.GroupB;
import com.coy.gupaoedu.study.mybatisplus.dto.BeanRequest;
import com.coy.gupaoedu.study.mybatisplus.dto.UserRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Bean validation demo
 *
 * @author chenck
 * @date 2019/9/10 12:15
 */
@RestController
public class BeanValidationDemoController {

    /**
     * 默认分组来进行校验，可以理解为没有定义groups分组的注解进行校验
     */
    @PostMapping(value = "/bean/validation/test")
    public DataResponse beanValidationTest(@RequestBody @Valid UserRequest request) {
        return new DataResponse();
    }

    /**
     * 只对GroupA分组进行校验
     */
    @PostMapping(value = "/bean/validation/test1")
    public DataResponse beanValidationTest1(@RequestBody @Validated(GroupA.class) UserRequest request) {
        return new DataResponse();
    }

     /**
     * 只对GroupB分组进行校验
     */
    @PostMapping(value = "/bean/validation/test2")
    public DataResponse beanValidationTest2(@RequestBody @Validated(GroupB.class) UserRequest request) {
        return new DataResponse();
    }

    /**
     * 对GroupA和GroupB分组进行校验
     */
    @PostMapping(value = "/bean/validation/test3")
    public DataResponse beanValidationTest3(@RequestBody @Validated({GroupA.class, GroupB.class}) UserRequest request) {
        return new DataResponse();
    }


    /**
     * 默认分组来进行校验，可以理解为没有定义groups分组的注解进行校验
     */
    @PostMapping(value = "/bean/validation/valuein")
    public DataResponse valueIntest(@RequestBody @Valid BeanRequest request) {
        return new DataResponse();
    }

}

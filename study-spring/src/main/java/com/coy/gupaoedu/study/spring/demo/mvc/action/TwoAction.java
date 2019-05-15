package com.coy.gupaoedu.study.spring.demo.mvc.action;

import com.coy.gupaoedu.study.spring.demo.service.IDemoService;
import com.coy.gupaoedu.study.spring.framework.mvc.annotation.GPAutowired;
import com.coy.gupaoedu.study.spring.framework.mvc.annotation.GPService;


@GPService
public class TwoAction {

    @GPAutowired
    private IDemoService demoService;

    public String getName(String name) {
        String result = demoService.get(name);
        return result;
    }

}

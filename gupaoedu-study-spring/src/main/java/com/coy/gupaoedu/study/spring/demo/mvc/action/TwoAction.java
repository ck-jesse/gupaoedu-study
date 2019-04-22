package com.coy.gupaoedu.study.spring.demo.mvc.action;

import com.coy.gupaoedu.study.spring.demo.service.IDemoService;
import com.coy.gupaoedu.study.spring.framework.mvc.annotation.GPAutowired;


public class TwoAction {

    @GPAutowired
    private IDemoService demoService;

    public void getName(String name) {
        String result = demoService.get(name);
        System.out.println(result);
    }

}

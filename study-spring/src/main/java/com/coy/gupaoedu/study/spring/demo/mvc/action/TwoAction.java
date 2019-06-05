package com.coy.gupaoedu.study.spring.demo.mvc.action;

import com.coy.gupaoedu.study.spring.demo.service.DemoService;
import com.coy.gupaoedu.study.spring.framework.beans.annotation.GPAutowired;
import com.coy.gupaoedu.study.spring.framework.context.annotation.GPService;


@GPService
public class TwoAction {

    @GPAutowired
    private DemoService demoService;

    public String getName(String name) {
        String result = demoService.get(name);
        return result;
    }

}

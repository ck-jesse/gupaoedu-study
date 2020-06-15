package com.coy.gupaoedu.study.spring.circularreference;

import com.hs.platform.log.access.annotation.LogAccess;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chenck
 * @date 2020/6/12 12:17
 */
@LogAccess
//@Component
public class ServiceB {

    @Autowired
    ServiceA serviceA;

    public void print(){
        System.out.println("ServiceB");
    }
}

package com.coy.gupaoedu.study.spring.demo.aspect;

import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInvocation;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * 日志切面
 *
 * @author chenck
 * @date 2019/4/22 13:46
 */
@Slf4j
public class LogAspect {

    //在调用一个方法之前，执行before方法
    public void before(GPMethodInvocation joinPoint) {
        joinPoint.setUserAttribute("startTime_" + joinPoint.getMethod().getName(), System.currentTimeMillis());
        //这个方法中的逻辑，是由我们自己写的
        log.info("Invoker Before Method!!!" +
                "\nTargetObject:" + joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
        System.out.println("Invoker Before Method!!!" +
                "\nTargetObject:" + joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
    }

    //在调用一个方法之后，执行after方法
    public void after(GPMethodInvocation joinPoint) {
        log.info("Invoker After Method!!!" +
                "\nTargetObject:" + joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
        System.out.println("Invoker After Method!!!" +
                "\nTargetObject:" + joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()));
        long startTime = (Long) joinPoint.getUserAttribute("startTime_" + joinPoint.getMethod().getName());
        long endTime = System.currentTimeMillis();
        System.out.println("use time :" + (endTime - startTime));
    }

    public void afterThrowing(GPMethodInvocation joinPoint, Throwable ex) {
        log.info("出现异常" +
                "\nTargetObject:" + joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()) +
                "\nThrows:" + ex.getMessage());
        System.out.println("出现异常" +
                "\nTargetObject:" + joinPoint.getThis() +
                "\nArgs:" + Arrays.toString(joinPoint.getArguments()) +
                "\nThrows:" + ex.getMessage());
    }

}

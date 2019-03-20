package com.coy.gupaoedu.study.pattern.proxy.dynamicproxy.cglib;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 日志代理拦截器
 * 
 * @author chenck
 * @date 2017年3月15日 下午2:51:41
 */
public class LogProxyIntercepter extends AbstractProxyIntercepter {

    @Override
    public Object beforeInvoke(Object targetObject1, Method method, Object[] args,
            MethodProxy proxy) {
        System.out.println("[CGLIB] beforeInvoke LOG");
        return null;
    }

    @Override
    public Object afterInvoke(Object targetObject1, Method method, Object[] args,
            MethodProxy proxy) {
        System.out.println("[CGLIB] afterInvoke LOG");
        return null;
    }

}

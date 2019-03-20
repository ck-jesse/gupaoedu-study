package com.coy.gupaoedu.study.pattern.proxy.dynamicproxy.jdk;

import java.lang.reflect.Method;

/**
 * 权限代理处理器
 * 
 * @author chenck
 * @date 2017年3月15日 下午2:51:41
 */
public class AuthProxyHandler extends AbstractProxyHandler {

    @Override
    public Object beforeInvoke(Object proxy, Method method, Object[] args) {
        System.out.println("[JDK] beforeInvoke Auth");
        return null;
    }

    @Override
    public Object afterInvoke(Object proxy, Method method, Object[] args) {
        System.out.println("[JDK] afterInvoke Auth");
        return null;
    }

}

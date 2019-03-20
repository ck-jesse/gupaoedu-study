package com.coy.gupaoedu.study.pattern.proxy.dynamicproxy.jdk;

import java.lang.reflect.Method;

/**
 * 日志代理处理器
 * 
 * @author chenck
 * @date 2017年3月15日 下午2:51:41
 */
public class LogProxyHandler extends AbstractProxyHandler {

    @Override
    public Object beforeInvoke(Object proxy, Method method, Object[] args) {
        System.out.println("[JDK] beforeInvoke LOG");
        return null;
    }

    @Override
    public Object afterInvoke(Object proxy, Method method, Object[] args) {
        System.out.println("[JDK] afterInvoke LOG");
        return null;
    }

}

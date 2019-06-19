package com.coy.gupaoedu.study.server.rpc;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * rpc通信 客户端请求处理器
 *
 * @author chenck
 * @date 2019/6/19 15:33
 */
@Component
public class RpcInvoker {

    /**
     * rpc 服务发布（注册服务到Map容器）
     */
    @Resource
    RpcServicePublisher servicePublisher;

    public Object invoke(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        String serviceName = rpcRequest.getClassName();
        String version = rpcRequest.getVersion();
        // 增加版本号的判断
        if (!StringUtils.isEmpty(version)) {
            serviceName += "-" + version;
        }
        // 获取服务bean
        Object service = servicePublisher.getHandlerMap().get(serviceName);
        if (service == null) {
            throw new RuntimeException("service not found:" + serviceName);
        }
        // 拿到客户端请求的参数
        Object[] args = rpcRequest.getParameters();
        Class[] types = null;
        if (null != args) {
            // 获得每个参数的类型
            types = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].getClass();
            }
        }
        // 根据请求的类进行加载
        Class clazz = Class.forName(rpcRequest.getClassName());
        // 获取对应的method
        Method method = clazz.getMethod(rpcRequest.getMethodName(), types); //sayHello, saveUser找到这个类中的方法
        // 反射调用
        Object result = method.invoke(service, args);
        return result;
    }

}

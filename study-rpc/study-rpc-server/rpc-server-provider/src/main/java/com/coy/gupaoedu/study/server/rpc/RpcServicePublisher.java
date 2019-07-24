package com.coy.gupaoedu.study.server.rpc;

import com.alibaba.fastjson.JSON;
import com.coy.gupaoedu.study.server.registry.RegistryCenter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * rpc 服务发布（注册服务到Map容器）
 *
 * @author chenck
 * @date 2019/6/6 17:20
 */
@Component
public class RpcServicePublisher implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    /**
     * 服务处理器bean
     */
    private Map<String, Object> handlerMap = new HashMap();

    /**
     * 发布服务
     */
    public void publisher() throws UnknownHostException {
        // 获取所有待发布的服务
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (serviceBeanMap.isEmpty()) {
            System.out.println("[server]no service need to publish");
            return;
        }
        String address = InetAddress.getLocalHost().getHostAddress();
        RegistryCenter registryCenter = applicationContext.getBean(RegistryCenter.class);

        for (Object servcieBean : serviceBeanMap.values()) {
            // 拿到注解
            RpcService rpcService = servcieBean.getClass().getAnnotation((RpcService.class));
            // 拿到接口类定义
            String serviceName = rpcService.value().getName();

            // 拿到版本号
            String version = rpcService.version();
            if (!StringUtils.isEmpty(version)) {
                serviceName += "-" + version;
            }
            System.out.println("[server]publish service " + serviceName);
            // 将服务添加到容器中
            handlerMap.put(serviceName, servcieBean);

            RpcUrl rpcUrl = new RpcUrl();
            rpcUrl.setHost(address);
            //rpcUrl.setPort(8080);
            rpcUrl.setInterfaceName(rpcService.value().getName());
            rpcUrl.setVersion(rpcService.version());

            Method[] methods = rpcService.value().getDeclaredMethods();
            String methodNames = "";
            for (Method method : methods) {
                if (method.getName().equals("hashcode") || method.getName().equals("toString")) {
                    continue;
                }
                methodNames = method.getName() + "," + methodNames;
            }
            if (methodNames.endsWith(",")) {
                methodNames = methodNames.substring(0, methodNames.length() - 1);
            }
            rpcUrl.setMethodNames(methodNames);
            rpcUrl.setSide("providers");
            System.out.println(JSON.toJSONString(rpcUrl));

            // 将该服务发布到注册中心
            registryCenter.registry(rpcUrl);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 发布服务
        publisher();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Map<String, Object> getHandlerMap() {
        return handlerMap;
    }
}

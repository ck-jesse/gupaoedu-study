package com.coy.gupaoedu.study.server.rpc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * RPC 服务发布
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
    public void publisher() {
        // 获取所有待发布的服务
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (serviceBeanMap.isEmpty()) {
            System.out.println("[server]no service need to publish");
            return;
        }
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
            handlerMap.put(serviceName, servcieBean);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 发布服务
        publisher();

        // 启动socket服务监听
        RpcServer rpcServer = applicationContext.getBean(RpcServer.class);
        rpcServer.reciveRequest(handlerMap);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

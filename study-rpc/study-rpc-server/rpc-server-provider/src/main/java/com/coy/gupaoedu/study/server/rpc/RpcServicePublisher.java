package com.coy.gupaoedu.study.server.rpc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenck
 * @date 2019/6/6 17:20
 */
@Component
public class RpcServicePublisher implements ApplicationContextAware, InitializingBean {

    private final int port;

    ExecutorService executorService = Executors.newCachedThreadPool();

    private ApplicationContext applicationContext;

    /**
     * 服务处理器bean
     */
    private Map<String, Object> handlerMap = new HashMap();

    public RpcServicePublisher(int port) {
        this.port = port;
    }

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

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                // accept不断接受请求
                Socket socket = serverSocket.accept();//BIO
                // 每一个socket 交给一个Handler来处理
                executorService.execute(new RpcClientRequestHandler(socket, handlerMap));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

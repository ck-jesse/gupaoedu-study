package com.coy.gupaoedu.study.server.rpc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author chenck
 * @date 2019/6/6 17:20
 */
@Component
public class RpcServicePublisher implements ApplicationContextAware, InitializingBean {

    /**
     * 发布服务
     */
    public void publisher() {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}

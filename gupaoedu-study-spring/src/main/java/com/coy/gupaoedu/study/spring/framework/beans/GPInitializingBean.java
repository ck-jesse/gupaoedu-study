package com.coy.gupaoedu.study.spring.framework.beans;

/**
 * 初始化bean接口
 *
 * @author chenck
 * @date 2019/4/16 16:07
 */
public interface GPInitializingBean {

    /**
     * 实例化后的初始化方法
     */
    void afterPropertiesSet() throws Exception;
}

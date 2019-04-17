package com.coy.gupaoedu.study.spring.framework.aop.framework;

import java.io.Serializable;

/**
 * 代理配置超类，用于创建代理，以确保所有代理创建者具有一致的属性
 *
 * @author chenck
 * @date 2019/4/16 19:37
 */
public class GPProxyConfig implements Serializable {

    /**
     * 是否是直接代理目标类，而不只是代理特定接口
     * true 表示代理类，使用cglib代理
     * false 表示代理接口，使用JDK代理
     */
    private boolean proxyTargetClass = false;

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    /**
     * Return whether to proxy the target class directly as well as any interfaces.
     */
    public boolean isProxyTargetClass() {
        return this.proxyTargetClass;
    }

}

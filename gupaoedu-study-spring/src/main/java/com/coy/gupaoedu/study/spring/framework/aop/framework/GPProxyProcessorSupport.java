package com.coy.gupaoedu.study.spring.framework.aop.framework;


/**
 * 具有代理处理器通用功能的基类
 *
 * @author chenck
 * @date 2019/4/16 19:48
 */
public class GPProxyProcessorSupport extends GPProxyConfig {

    /**
     * 类加载器，默认值为当前线程的类加载器
     */
    private ClassLoader proxyClassLoader = Thread.currentThread().getContextClassLoader();

    public void setProxyClassLoader(ClassLoader classLoader) {
        this.proxyClassLoader = classLoader;
    }

    protected ClassLoader getProxyClassLoader() {
        return this.proxyClassLoader;
    }
}

package com.coy.gupaoedu.study.spring.framework.beans;

/**
 * 销毁bean接口
 * 希望在销毁时释放资源的bean要实现的接口
 *
 * @author chenck
 * @date 2019/4/18 14:40
 */
public interface GPDisposableBean {

    /**
     * 由BeanFactory在销毁单个实例时调用
     * <p>
     * Invoked by a BeanFactory on destruction of a singleton.
     *
     * @throws Exception in case of shutdown errors.
     *                   Exceptions will get logged but not rethrown to allow
     *                   other beans to release their resources too.
     */
    void destroy() throws Exception;
}

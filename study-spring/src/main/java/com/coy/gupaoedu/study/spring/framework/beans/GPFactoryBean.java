package com.coy.gupaoedu.study.spring.framework.beans;

/**
 * 工厂bean：创建bean对象的工厂Bean
 * <p>
 * 可以生成某一个类型Bean实例，它最大的一个作用是：可以让我们自定义Bean的创建过程。
 *
 * @author chenck
 * @date 2019/4/10 21:26
 */
public interface GPFactoryBean<T> {

    /**
     * 获取对象实例(从容器中获取)
     */
    T getObject() throws Exception;

    /**
     * 获取对象类型
     */
    Class<?> getObjectType();

    /**
     * 是否单例
     * 基于jdk1.8的特性实现默认方法
     */
    default boolean isSingleton() {
        return true;
    }

}

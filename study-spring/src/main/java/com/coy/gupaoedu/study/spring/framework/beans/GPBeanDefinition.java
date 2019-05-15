package com.coy.gupaoedu.study.spring.framework.beans;

import lombok.Data;

/**
 * @author chenck
 * @date 2019/4/10 21:57
 */
@Data
public class GPBeanDefinition {

    /**
     * Constant for the default scope name: {@code ""}, equivalent to singleton
     * status unless overridden from a parent bean definition (if applicable).
     */
    public static final String SCOPE_DEFAULT = "";

    /**
     * Scope identifier for the standard singleton scope: "singleton".
     * <p>Note that extended bean factories might support further scopes.
     *
     * @see #setScope
     */
    public static final String SCOPE_SINGLETON = "singleton";

    /**
     * Scope identifier for the standard prototype scope: "prototype".
     * <p>Note that extended bean factories might support further scopes.
     *
     * @see #setScope
     */
    public static final String SCOPE_PROTOTYPE = "prototype";

    /**
     * bean名称
     */
    private String factoryBeanName;
    /**
     * bean的Class名称（类全路径）
     */
    private String beanClassName;
    /**
     * Bean的Class对象
     */
    private Object beanClass;
    /**
     * 作用域
     */
    private String scope = "";
    /**
     * 抽象类标志
     */
    private boolean abstractFlag = false;
    /**
     * 延迟加载
     */
    private boolean lazyInit = false;
    /**
     * 0表示自动注入
     */
    private int autowireMode = 0;
    /**
     * 初始化方法名
     */
    private String initMethodName;
    /**
     * 销毁方法名
     */
    private String destroyMethodName;

    /**
     * 是否是直接代理目标类，而不只是代理特定接口
     * true 表示代理类，使用cglib代理
     * false 表示代理接口，使用JDK代理
     */
    private boolean proxyTargetClass = false;

    /**
     * 判断是否是单利
     */
    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
    }

    /**
     * 判断是否是
     */
    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(scope);
    }

    /**
     * Return whether this definition specifies a bean class.
     */
    public boolean hasBeanClass() {
        return (this.beanClass instanceof Class);
    }

    /**
     *
     */
    public Class<?> getBeanClazz() throws IllegalStateException {
        Object beanClassObject = this.beanClass;
        if (beanClassObject == null) {
            throw new IllegalStateException("No bean class specified on bean definition");
        }
        if (!(beanClassObject instanceof Class)) {
            throw new IllegalStateException(
                    "Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
        }
        return (Class<?>) beanClassObject;
    }
}

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
     * 延迟加载
     */
    private boolean lazyInit = false;
    /**
     * 0表示自动注入
     */
    private int autowireMode = 0;

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
}

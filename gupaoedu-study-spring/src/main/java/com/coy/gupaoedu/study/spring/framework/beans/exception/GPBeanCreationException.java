package com.coy.gupaoedu.study.spring.framework.beans.exception;


import com.sun.istack.internal.Nullable;

/**
 * @author chenck
 * @date 2019/4/29 17:23
 */
public class GPBeanCreationException extends GPBeansException {

    private String beanName;

    private String resourceDescription;

    /**
     * Create a new BeanCreationException.
     *
     * @param msg the detail message
     */
    public GPBeanCreationException(String msg) {
        super(msg);
    }

    /**
     * Create a new BeanCreationException.
     *
     * @param msg   the detail message
     * @param cause the root cause
     */
    public GPBeanCreationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Create a new BeanCreationException.
     *
     * @param beanName the name of the bean requested
     * @param msg      the detail message
     */
    public GPBeanCreationException(String beanName, String msg) {
        super("Error creating bean with name '" + beanName + "': " + msg);
        this.beanName = beanName;
    }

    /**
     * Create a new BeanCreationException.
     *
     * @param beanName the name of the bean requested
     * @param msg      the detail message
     * @param cause    the root cause
     */
    public GPBeanCreationException(String beanName, String msg, Throwable cause) {
        this(beanName, msg);
        initCause(cause);
    }

    /**
     * Create a new BeanCreationException.
     *
     * @param resourceDescription description of the resource
     *                            that the bean definition came from
     * @param beanName            the name of the bean requested
     * @param msg                 the detail message
     */
    public GPBeanCreationException(String resourceDescription, String beanName, String msg) {
        super("Error creating bean with name '" + beanName + "'" +
                (resourceDescription != null ? " defined in " + resourceDescription : "") + ": " + msg);
        this.resourceDescription = resourceDescription;
        this.beanName = beanName;
    }

    /**
     * Create a new BeanCreationException.
     *
     * @param resourceDescription description of the resource
     *                            that the bean definition came from
     * @param beanName            the name of the bean requested
     * @param msg                 the detail message
     * @param cause               the root cause
     */
    public GPBeanCreationException(@Nullable String resourceDescription, String beanName, String msg, Throwable cause) {
        this(resourceDescription, beanName, msg);
        initCause(cause);
    }

    public String getBeanName() {
        return beanName;
    }

    public String getResourceDescription() {
        return resourceDescription;
    }
}

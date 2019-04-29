package com.coy.gupaoedu.study.spring.framework.beans.exception;

/**
 * @author chenck
 * @date 2019/4/29 17:25
 */
public class GPBeanCurrentlyInCreationException extends GPBeanCreationException {
    /**
     * Create a new GPBeanCurrentlyInCreationException,
     * with a default error message that indicates a circular reference.
     *
     * @param beanName the name of the bean requested
     */
    public GPBeanCurrentlyInCreationException(String beanName) {
        super(beanName, "Requested bean is currently in creation: Is there an unresolvable circular reference?");
    }

    public GPBeanCurrentlyInCreationException(String beanName, String msg) {
        super(beanName, msg);
    }

}

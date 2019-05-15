package com.coy.gupaoedu.study.spring.framework.beans;

/**
 * @author chenck
 * @date 2019/4/10 22:29
 */
public class GPBeanWrapper {

    Object wrappedObject;

    public GPBeanWrapper(Object object) {
        wrappedObject = object;
    }

    public Object getWrappedInstance() {
        return this.wrappedObject;
    }

    public Class<?> getWrappedClass() {
        return getWrappedInstance().getClass();
    }
}

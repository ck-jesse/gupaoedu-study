package com.coy.gupaoedu.study.spring.framework.beans.factory.config;

import com.coy.gupaoedu.study.spring.framework.beans.ObjectFactory;

/**
 * Strategy interface used by a {@link ConfigurableBeanFactory},
 * representing a target scope to hold bean instances in.
 *
 * @author chenck
 * @date 2019/4/16 18:02
 */
public interface GPScope {

    /**
     * Return the object with the given name from the underlying scope
     */
    Object get(String name, ObjectFactory<?> objectFactory);

    /**
     * Remove the object with the given {@code name} from the underlying scope
     */
    Object remove(String name);
}

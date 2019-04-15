package com.coy.gupaoedu.study.spring.framework.beans.support;

import com.coy.gupaoedu.study.spring.framework.beans.factory.config.SingletonBeanRegistry;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenck
 * @date 2019/4/14 18:49
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /**
     * Cache of singleton objects: bean name --> bean instance
     * 单例的IOC容器缓存
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(256);

    /**
     * Set of registered singletons, containing the bean names in registration order
     */
    private final Set<String> registeredSingletons = new LinkedHashSet<String>(256);

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            Object oldObject = this.singletonObjects.get(beanName);
            if (oldObject != null) {
                throw new IllegalStateException("Could not register object [" + singletonObject +
                        "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
            }
            addSingleton(beanName, singletonObject);
        }
    }

    /**
     * Add the given singleton object to the singleton cache of this factory.
     * <p>To be called for eager registration of singletons.
     */
    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.registeredSingletons.add(beanName);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return getSingleton(beanName, true);
    }

    /**
     * Return the (raw) singleton object registered under the given name
     */
    protected Object getSingleton(String beanName, boolean allowEarlyReference) {
        if (this.singletonObjects.containsKey(beanName)) {
            return this.singletonObjects.get(beanName);
        }
        Object singletonObject = null;
        try {
            Class<?> clazz = Class.forName(beanName);
            singletonObject = clazz.newInstance();
            registerSingleton(beanName, singletonObject);
            registerSingleton(clazz.getSimpleName(), singletonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return singletonObject;
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        synchronized (this.singletonObjects) {
            return registeredSingletons.toArray(new String[registeredSingletons.size()]);
        }
    }

    @Override
    public int getSingletonCount() {
        synchronized (this.singletonObjects) {
            return this.registeredSingletons.size();
        }
    }

    @Override
    public final Object getSingletonMutex() {
        return this.singletonObjects;
    }
}

package com.coy.gupaoedu.study.spring.framework.beans.support;

import com.coy.gupaoedu.study.spring.framework.beans.GPDisposableBean;
import com.coy.gupaoedu.study.spring.framework.beans.ObjectFactory;
import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeanCurrentlyInCreationException;
import com.coy.gupaoedu.study.spring.framework.beans.factory.config.SingletonBeanRegistry;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
     * 单例bean的IOC容器缓存（该bean已经始化完全）
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(256);

    /**
     * Cache of singleton factories: bean name --> ObjectFactory
     * 缓存单例bean的工厂，关键在ObjectFactory，可以通过该ObjectFactory.getObject()拿到对象（注：该bean还未初始化完全）
     */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    /**
     * Cache of early singleton objects: bean name --> bean instance
     * 缓存刚创建的单例bean，以便可以提前曝光该bean（该bean还未初始化完全），主要用于解决循环依赖的问题
     */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);

    /**
     * Set of registered singletons, containing the bean names in registration order
     * 注册的单例集合，包含按注册顺序排列的bean名称
     */
    private final Set<String> registeredSingletons = new LinkedHashSet<String>(256);

    /**
     * Names of beans that are currently in creation
     * 当前正在创建中的单例bean的名称集合
     */
    private final Set<String> singletonsCurrentlyInCreation = Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    /**
     * Disposable bean instances: bean name --> disposable instance
     * 可释放的实例集合
     */
    private final Map<String, Object> disposableBeans = new LinkedHashMap<>();

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
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }

    /**
     * 如有必要，添加用于生成指定单例的bean指定单例工厂
     */
    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        Assert.notNull(singletonFactory, "Singleton factory must not be null");
        synchronized (this.singletonObjects) {
            if (!this.singletonObjects.containsKey(beanName)) {
                this.singletonFactories.put(beanName, singletonFactory);
                this.earlySingletonObjects.remove(beanName);
                this.registeredSingletons.add(beanName);
            }
        }
    }

    /**
     * Return the (raw) singleton object registered under the given name
     */
    @Override
    public Object getSingleton(String beanName) {
        return getSingleton(beanName, true);
    }

    /**
     * Return the (raw) singleton object registered under the given name.
     * <p>Checks already instantiated singletons and also allows for an early
     * reference to a currently created singleton (resolving a circular reference).
     *
     * @param beanName            the name of the bean to look for
     * @param allowEarlyReference whether early references should be created or not
     * @return the registered singleton object, or {@code null} if none found
     */
    protected Object getSingleton(String beanName, boolean allowEarlyReference) {
        // 单例bean一级缓存(缓存的bean已经初始化完全)
        Object singletonObject = this.singletonObjects.get(beanName);
        if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
            synchronized (this.singletonObjects) {
                // 单例bean二级缓存(缓存的bean还没有初始化完全)
                singletonObject = this.earlySingletonObjects.get(beanName);
                if (singletonObject == null && allowEarlyReference) {
                    // 单例bean三级缓存（缓存了还没有初始化完全的bean的工厂）
                    ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                    if (singletonFactory != null) {
                        // 通过单例bean工厂获取提前暴露的单例bean，若提前暴露的单例bean符合aop拦截器，则会创建代理对象
                        singletonObject = singletonFactory.getObject();
                        this.earlySingletonObjects.put(beanName, singletonObject);
                        this.singletonFactories.remove(beanName);
                    }
                }
            }
        }
        return singletonObject;
    }

    /**
     * Return the (raw) singleton object registered under the given name
     */
    public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
        synchronized (this.singletonObjects) {
            Object singletonObject = this.singletonObjects.get(beanName);
            if (null == singletonObject) {
                // 将该单例bean注册到当前正在创建中的容器
                beforeSingletonCreation(beanName);
                try {
                    // 内部有调用创建bean的方法
                    singletonObject = singletonFactory.getObject();
                    // 将单例对象添加到缓存中
                    addSingleton(beanName, singletonObject);
                } catch (Exception e) {
                    singletonObject = this.singletonObjects.get(beanName);
                    if (singletonObject == null) {
                        throw e;
                    }
                } finally {
                    // 将单例bean从创建中的容器中移除
                    afterSingletonCreation(beanName);
                }
            }
            return singletonObject;
        }
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

    /**
     * 将给定的bean添加到此注册表中的一次性bean列表中
     */
    public void registerDisposableBean(String beanName, GPDisposableBean bean) {
        synchronized (this.disposableBeans) {
            this.disposableBeans.put(beanName, bean);
        }
    }

    /**
     * Remove a registered singleton of the given name,
     */
    protected void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.remove(beanName);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.remove(beanName);
        }

        // TODO
        GPDisposableBean disposableBean;
        synchronized (this.disposableBeans) {
            disposableBean = (GPDisposableBean) this.disposableBeans.remove(beanName);
        }
    }


    /**
     * 判断该单例bean是否正在创建中
     * Return whether the specified singleton bean is currently in creation
     * (within the entire factory).
     *
     * @param beanName the name of the bean
     */
    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }

    /**
     * 在创建单例bean之前回调。默认将单例bean注册到当前正在创建中的容器
     * Callback before singleton creation.
     * <p>The default implementation register the singleton as currently in creation.
     *
     * @param beanName the name of the singleton about to be created
     * @see #isSingletonCurrentlyInCreation
     */
    protected void beforeSingletonCreation(String beanName) {
        if (!this.singletonsCurrentlyInCreation.add(beanName)) {
            throw new GPBeanCurrentlyInCreationException(beanName);
        }
    }

    /**
     * 在创建单例bean之后回调。默认将单例bean从创建中的容器中移除
     * Callback after singleton creation.
     * <p>The default implementation marks the singleton as not in creation anymore.
     *
     * @param beanName the name of the singleton that has been created
     * @see #isSingletonCurrentlyInCreation
     */
    protected void afterSingletonCreation(String beanName) {
        if (!this.singletonsCurrentlyInCreation.remove(beanName)) {
            throw new IllegalStateException("Singleton '" + beanName + "' isn't currently in creation");
        }
    }
}

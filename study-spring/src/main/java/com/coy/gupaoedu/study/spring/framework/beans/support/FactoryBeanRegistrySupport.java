package com.coy.gupaoedu.study.spring.framework.beans.support;

import com.coy.gupaoedu.study.spring.framework.beans.GPFactoryBean;
import com.coy.gupaoedu.study.spring.framework.beans.exception.FactoryBeanNotInitializedException;
import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeanCreationException;
import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeanCurrentlyInCreationException;
import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeansException;
import com.sun.istack.internal.Nullable;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对于实现FactoryBean的子类，自定义创建bean的过程的扩展，同时作为容器缓存bean
 *
 * @author chenck
 * @date 2019/6/3 19:58
 */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    /**
     * Cache of singleton objects created by FactoryBeans: FactoryBean name --> object
     */
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>(16);

    /**
     * Determine the type for the given FactoryBean.
     *
     * @param factoryBean the FactoryBean instance to check
     * @return the FactoryBean's object type,
     * or {@code null} if the type cannot be determined yet
     */
    @Nullable
    protected Class<?> getTypeForFactoryBean(final GPFactoryBean<?> factoryBean) {
        try {
            if (System.getSecurityManager() != null) {
                return AccessController.doPrivileged((PrivilegedAction<Class<?>>) () ->
                        factoryBean.getObjectType(), getAccessControlContext());
            } else {
                return factoryBean.getObjectType();
            }
        } catch (Throwable ex) {
            // Thrown from the FactoryBean's getObjectType implementation.
            logger.warn("FactoryBean threw exception from getObjectType, despite the contract saying " +
                    "that it should return null if the type of its object cannot be determined yet", ex);
            return null;
        }
    }


    /**
     * Post-process the given object that has been obtained from the FactoryBean.
     * The resulting object will get exposed for bean references.
     * <p>The default implementation simply returns the given object as-is.
     * Subclasses may override this, for example, to apply post-processors.
     *
     * @param object   the object obtained from the FactoryBean.
     * @param beanName the name of the bean
     * @return the object to expose
     */
    protected Object postProcessObjectFromFactoryBean(Object object, String beanName) throws GPBeansException {
        return object;
    }


    /**
     * Obtain an object to expose from the given FactoryBean.
     *
     * @param factory           the FactoryBean instance
     * @param beanName          the name of the bean
     * @param shouldPostProcess whether the bean is subject to post-processing
     * @return the object obtained from the FactoryBean
     */
    //Bean工厂生产Bean实例对象
    protected Object getObjectFromFactoryBean(GPFactoryBean<?> factory, String beanName, boolean shouldPostProcess) {
        //Bean工厂是单态模式，并且Bean工厂缓存中存在指定名称的Bean实例对象
        if (factory.isSingleton() && containsSingleton(beanName)) {
            //多线程同步，以防止数据不一致
            synchronized (getSingletonMutex()) {
                //直接从Bean工厂缓存中获取指定名称的Bean实例对象
                Object object = this.factoryBeanObjectCache.get(beanName);
                //Bean工厂缓存中没有指定名称的实例对象，则生产该实例对象
                if (object == null) {
                    //调用Bean工厂的getObject方法生产指定Bean的实例对象
                    object = doGetObjectFromFactoryBean(factory, beanName);
                    // Only post-process and store if not put there already during getObject() call above
                    // (e.g. because of circular reference processing triggered by custom getBean calls)
                    Object alreadyThere = this.factoryBeanObjectCache.get(beanName);
                    if (alreadyThere != null) {
                        object = alreadyThere;
                    } else {
                        if (shouldPostProcess) {
                            try {
                                object = postProcessObjectFromFactoryBean(object, beanName);
                            } catch (Throwable ex) {
                                throw new GPBeanCreationException(beanName,
                                        "Post-processing of FactoryBean's singleton object failed", ex);
                            }
                        }
                        //将生产的实例对象添加到Bean工厂缓存中
                        this.factoryBeanObjectCache.put(beanName, object);
                    }
                }
                return object;
            }
        }
        //调用Bean工厂的getObject方法生产指定Bean的实例对象
        else {
            Object object = doGetObjectFromFactoryBean(factory, beanName);
            if (shouldPostProcess) {
                try {
                    object = postProcessObjectFromFactoryBean(object, beanName);
                } catch (Throwable ex) {
                    throw new GPBeanCreationException(beanName, "Post-processing of FactoryBean's object failed", ex);
                }
            }
            return object;
        }
    }

    /**
     * Obtain an object to expose from the given FactoryBean.
     *
     * @param factory  the FactoryBean instance
     * @param beanName the name of the bean
     * @return the object obtained from the FactoryBean
     */
    //调用Bean工厂的getObject方法生产指定Bean的实例对象
    private Object doGetObjectFromFactoryBean(final GPFactoryBean<?> factory, final String beanName)
            throws GPBeanCreationException {

        Object object;
        try {
            if (System.getSecurityManager() != null) {
                AccessControlContext acc = getAccessControlContext();
                try {
                    //实现PrivilegedExceptionAction接口的匿名内置类
                    //根据JVM检查权限，然后决定BeanFactory创建实例对象
                    object = AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () ->
                            factory.getObject(), acc);
                } catch (PrivilegedActionException pae) {
                    throw pae.getException();
                }
            } else {
                //调用BeanFactory接口实现类的创建对象方法
                object = factory.getObject();
            }
        } catch (FactoryBeanNotInitializedException ex) {
            throw new GPBeanCurrentlyInCreationException(beanName, ex.toString());
        } catch (Throwable ex) {
            throw new GPBeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
        }

        // Do not accept a null value for a FactoryBean that's not fully
        // initialized yet: Many FactoryBeans just return null then.
        //创建出来的实例对象为null，或者因为单态对象正在创建而返回null
        if (object == null) {
            if (isSingletonCurrentlyInCreation(beanName)) {
                throw new GPBeanCurrentlyInCreationException(
                        beanName, "FactoryBean which is currently in creation returned null from getObject");
            }
            object = new NullBean();
        }
        return object;
    }


    /**
     * Get a FactoryBean for the given bean if possible.
     *
     * @param beanName     the name of the bean
     * @param beanInstance the corresponding bean instance
     * @return the bean instance as FactoryBean
     */
    protected GPFactoryBean<?> getFactoryBean(String beanName, Object beanInstance) throws GPBeansException {
        if (!(beanInstance instanceof GPFactoryBean)) {
            throw new GPBeanCreationException(beanName,
                    "Bean instance of type [" + beanInstance.getClass() + "] is not a FactoryBean");
        }
        return (GPFactoryBean<?>) beanInstance;
    }

    /**
     * Overridden to clear the FactoryBean object cache as well.
     */
    @Override
    protected void removeSingleton(String beanName) {
        super.removeSingleton(beanName);
        this.factoryBeanObjectCache.remove(beanName);
    }

    /**
     * Obtain an object to expose from the given FactoryBean, if available
     * in cached form. Quick check for minimal synchronization.
     *
     * @param beanName the name of the bean
     * @return the object obtained from the FactoryBean,
     * or {@code null} if not available
     */
    protected Object getCachedObjectForFactoryBean(String beanName) {
        return this.factoryBeanObjectCache.get(beanName);
    }


    /**
     * Return the security context for this bean factory. If a security manager
     * is set, interaction with the user code will be executed using the privileged
     * of the security context returned by this method.
     *
     * @see AccessController#getContext()
     */
    protected AccessControlContext getAccessControlContext() {
        return AccessController.getContext();
    }

}

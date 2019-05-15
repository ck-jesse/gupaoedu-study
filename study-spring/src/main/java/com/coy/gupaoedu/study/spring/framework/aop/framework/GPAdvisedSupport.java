package com.coy.gupaoedu.study.spring.framework.aop.framework;

import com.coy.gupaoedu.study.spring.framework.aop.GPAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.GPAdvice;
import com.coy.gupaoedu.study.spring.framework.aop.support.GPDefaultPointcutAdvisor;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代理配置+工具类
 *
 * @author chenck
 * @date 2019/4/14 22:01
 */
public class GPAdvisedSupport extends GPProxyConfig implements GPAdvised {

    /**
     * Target cached and invoked using reflection
     * 目标对象
     */
    private Object target;

    /**
     * target的class
     */
    Class<?> targetClass;

    /**
     * Advisor 顾问链工厂
     */
    GPDefaultAdvisorChainFactory advisorChainFactory = new GPDefaultAdvisorChainFactory();

    /**
     * Cache with Method as key and advisor chain List as value
     */
    private transient Map<MethodCacheKey, List<Object>> methodCache;
    /**
     * Interfaces to be implemented by the proxy. Held in List to keep the order
     * of registration, to create JDK proxy with specified order of interfaces.
     * 目标对象的接口集
     */
    private List<Class<?>> interfaces = new ArrayList<>();

    /**
     * List of Advisors. If an Advice is added, it will be wrapped
     * in an Advisor before being added to this List.
     */
    private List<GPAdvisor> advisors = new LinkedList<>();


    public GPAdvisedSupport() {
        this.methodCache = new ConcurrentHashMap<>(32);
    }

    public GPAdvisedSupport(Object target) {
        this(target, null);
    }

    public GPAdvisedSupport(Class<?>... interfaces) {
        this(null, interfaces);
    }

    public GPAdvisedSupport(Object target, Class<?>... interfaces) {
        this();
        this.target = target;
        setInterfaces(interfaces);
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public void setTarget(Object target) {
        this.target = target;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    /**
     * 获取目标对象的Class对象
     */
    @Override
    public Class<?> getTargetClass() {
        if (null != this.targetClass) {
            return this.targetClass;
        }
        if (null != this.target) {
            return this.target.getClass();
        }
        return null;
    }

    @Override
    public Class<?>[] getProxiedInterfaces() {
        return this.interfaces.toArray(new Class<?>[this.interfaces.size()]);
    }

    @Override
    public boolean isInterfaceProxied(Class<?> intf) {
        for (Class<?> proxyIntf : this.interfaces) {
            if (intf.isAssignableFrom(proxyIntf)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public GPAdvisor[] getAdvisors() {
        return this.advisors.toArray(new GPAdvisor[this.advisors.size()]);
    }

    @Override
    public void addAdvisor(GPAdvisor advisor) {
        int pos = this.advisors.size();
        addAdvisor(pos, advisor);
    }

    @Override
    public void addAdvisor(int pos, GPAdvisor advisor) {
        Assert.notNull(advisor, "Advisor must not be null");
        if (pos > this.advisors.size()) {
            throw new IllegalArgumentException(
                    "Illegal position " + pos + " in advisor list with size " + this.advisors.size());
        }
        this.advisors.add(pos, advisor);
        adviceChanged();
    }

    @Override
    public boolean removeAdvisor(GPAdvisor advisor) {
        int index = indexOf(advisor);
        if (index == -1) {
            return false;
        } else {
            removeAdvisor(index);
            return true;
        }
    }

    @Override
    public void removeAdvisor(int index) {
        if (index < 0 || index > this.advisors.size() - 1) {
            throw new AopConfigException("Advisor index " + index + " is out of bounds: " +
                    "This configuration only has " + this.advisors.size() + " advisors.");
        }
        this.advisors.remove(index);
        adviceChanged();
    }

    /**
     * Add all of the given advisors to this proxy configuration.
     *
     * @param advisors the advisors to register
     */
    public void addAdvisors(GPAdvisor... advisors) {
        addAdvisors(Arrays.asList(advisors));
    }

    /**
     * Add all of the given advisors to this proxy configuration.
     *
     * @param advisors the advisors to register
     */
    public void addAdvisors(Collection<GPAdvisor> advisors) {
        if (null != advisors && advisors.size() > 0) {
            for (GPAdvisor advisor : advisors) {
                Assert.notNull(advisor, "Advisor must not be null");
                this.advisors.add(advisor);
            }
            adviceChanged();
        }
    }

    @Override
    public int indexOf(GPAdvisor advisor) {
        Assert.notNull(advisor, "Advisor must not be null");
        return this.advisors.indexOf(advisor);
    }

    @Override
    public void addAdvice(GPAdvice advice) throws AopConfigException {
        int pos = this.advisors.size();
        addAdvice(pos, advice);
    }

    @Override
    public void addAdvice(int pos, GPAdvice advice) throws AopConfigException {
        Assert.notNull(advice, "Advice must not be null");
        // 将通知Advice转换为顾问Advisor，并保存下来
        addAdvisor(pos, new GPDefaultPointcutAdvisor(advice));
    }

    @Override
    public boolean removeAdvice(GPAdvice advice) {
        int index = indexOf(advice);
        if (index == -1) {
            return false;
        } else {
            removeAdvisor(index);
            return true;
        }
    }

    /**
     *
     */
    @Override
    public int indexOf(GPAdvice advice) {
        Assert.notNull(advice, "Advice must not be null");
        for (int i = 0; i < this.advisors.size(); i++) {
            GPAdvisor advisor = this.advisors.get(i);
            if (advisor.getAdvice() == advice) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Set the interfaces to be proxied.
     */
    public void setInterfaces(Class<?>... interfaces) {
        Assert.notNull(interfaces, "Interfaces must not be null");
        this.interfaces.clear();
        for (Class<?> ifc : interfaces) {
            addInterface(ifc);
        }
    }

    /**
     * Add a new proxied interface.
     *
     * @param intf the additional interface to proxy
     */
    public void addInterface(Class<?> intf) {
        Assert.notNull(intf, "Interface must not be null");
        if (!intf.isInterface()) {
            throw new IllegalArgumentException("[" + intf.getName() + "] is not an interface");
        }
        if (!this.interfaces.contains(intf)) {
            this.interfaces.add(intf);
            adviceChanged();
        }
    }

    /**
     * Invoked when advice has changed.
     */
    protected void adviceChanged() {
        this.methodCache.clear();
    }


    /**
     * 获取method对应的MethodInterceptor拦截器列表
     *
     * @param method      代理方法
     * @param targetClass 目标对象class
     * @return MethodInterceptors拦截器列表
     */
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
        MethodCacheKey cacheKey = new MethodCacheKey(method);
        List<Object> cached = this.methodCache.get(cacheKey);
        if (cached == null) {
            cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(this, method, targetClass);
            this.methodCache.put(cacheKey, cached);
        }
        return cached;
    }


    /**
     * Simple wrapper class around a Method. Used as the key when
     * caching methods, for efficient equals and hashCode comparisons.
     */
    private static final class MethodCacheKey implements Comparable<MethodCacheKey> {

        private final Method method;

        private final int hashCode;

        public MethodCacheKey(Method method) {
            this.method = method;
            this.hashCode = method.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            return (this == other || (other instanceof MethodCacheKey &&
                    this.method == ((MethodCacheKey) other).method));
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        @Override
        public String toString() {
            return this.method.toString();
        }

        @Override
        public int compareTo(MethodCacheKey other) {
            int result = this.method.getName().compareTo(other.method.getName());
            if (result == 0) {
                result = this.method.toString().compareTo(other.method.toString());
            }
            return result;
        }
    }
}

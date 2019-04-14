package com.coy.gupaoedu.study.spring.framework.beans.support;

import com.coy.gupaoedu.study.spring.framework.beans.GPBeanDefinition;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanWrapper;
import com.coy.gupaoedu.study.spring.framework.beans.exception.BeanException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenck
 * @date 2019/4/10 21:43
 */
public class GPDefaultListableBeanFactory implements GPBeanFactory {

    /**
     * Map of bean definition objects, keyed by bean name
     * Map<BeanName, GPBeanDefinition>
     */
    private final Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap(256);

    /**
     * List of bean definition names, in registration order
     */
    private volatile List<String> beanDefinitionNames = new ArrayList<String>(256);

    /**
     * Cache of unfinished FactoryBean instances: FactoryBean name --> BeanWrapper
     */
    private final Map<String, GPBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, GPBeanWrapper>(16);

    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName, null, null, false);
    }

    @Override
    public <T> T getBean(Class<T> beanClazz) {
        return (T) this.getBean(beanClazz.getName());
    }

    /**
     * Return an instance, which may be shared or independent, of the specified bean
     * 真正实现向IOC容器获取Bean的功能，也是触发依赖注入功能的地方
     */
    protected <T> T doGetBean(String beanName, Class<T> beanClazz, Object[] args, boolean typeCheckOnly) {
        GPBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        // 初始化bean实例
        Object instance = instanceBean(beanName, beanDefinition);

        GPBeanWrapper beanWrapper = new GPBeanWrapper(instance);

        // 将bean存放到IOC容器中
        this.factoryBeanInstanceCache.put(beanName, beanWrapper);

        // 注入
        return null;
    }

    private Object instanceBean(String beanName, GPBeanDefinition beanDefinition) {
        return null;
    }

    @Override
    public GPBeanDefinition getBeanDefinition(String beanName) {
        return this.beanDefinitionMap.get(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionNames.toArray(new String[beanDefinitionNames.size()]);
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    @Override
    public void registerBeanDefinition(String beanName, GPBeanDefinition beanDefinition) {
        if (this.beanDefinitionMap.containsKey(beanName)) {
            throw new BeanException(beanName + " BeanDefinition is already exists");
        }
        // 此处简单是实现，暂不考虑多线程的情况
        this.beanDefinitionMap.put(beanName, beanDefinition);
        this.beanDefinitionNames.add(beanName);
    }

    @Override
    public void preInstantiateSingletons() {
        // Iterate over a copy to allow for init methods which in turn register new bean definitions.
        // While this may not be part of the regular factory bootstrap, it does otherwise work fine.
        // 因beanDefinitionNames定义为了volatile，所以此处复制一个副本进行处理，避免迭代处理过程中集合的内容有新增的情况
        List<String> beanNames = new ArrayList<String>(this.beanDefinitionNames);

        // 触发所有非延迟加载的单例bean的初始化
        for (String beanName : beanNames) {
            GPBeanDefinition bd = this.beanDefinitionMap.get(beanName);
            if (bd.isSingleton() && !bd.isLazyInit()) {
                this.getBean(beanName);
            }
        }

        // TODO 触发所有适用bean的初始化后回调
    }
}

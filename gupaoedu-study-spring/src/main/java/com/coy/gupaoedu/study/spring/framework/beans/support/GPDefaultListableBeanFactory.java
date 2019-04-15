package com.coy.gupaoedu.study.spring.framework.beans.support;

import com.coy.gupaoedu.study.spring.framework.beans.GPBeanDefinition;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanWrapper;
import com.coy.gupaoedu.study.spring.framework.beans.exception.BeanException;
import com.coy.gupaoedu.study.spring.framework.beans.factory.config.GPBeanPostProcessor;
import com.coy.gupaoedu.study.spring.framework.mvc.annotation.GPAutowired;
import com.coy.gupaoedu.study.spring.framework.mvc.annotation.GPController;
import com.coy.gupaoedu.study.spring.framework.mvc.annotation.GPService;
import com.sun.istack.internal.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenck
 * @date 2019/4/10 21:43
 */
public class GPDefaultListableBeanFactory extends DefaultSingletonBeanRegistry implements GPBeanFactory {

    /**
     * Map of bean definition objects, keyed by bean name
     * Bean定义集合 Map<BeanName, GPBeanDefinition>
     */
    private final Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap(256);

    /**
     * List of bean definition names, in registration order
     * beanName集合
     */
    private volatile List<String> beanDefinitionNames = new ArrayList<String>(256);

    /**
     * Cache of unfinished FactoryBean instances: FactoryBean name --> BeanWrapper
     * 通用的IOC容器
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

        // 获取BeanDefinition
        GPBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);

        // TODO 此处暂时先简单处理
        // 获取单利bean
        Object singletonInstance = super.getSingleton(beanName);
        if (null != singletonInstance) {
            return (T) singletonInstance;
        }

        if (beanDefinition.isSingleton()) {
            //createBean();
        } else if (beanDefinition.isPrototype()) {
            //createBean();
        } else {

        }

        // createBean的内容如下：
        // 初始化bean实例
        GPBeanWrapper beanWrapper = instantiateBean(beanName, beanDefinition);
        Object beanInstance = beanWrapper.getWrappedInstance();

        //这个逻辑还不严谨，自己可以去参考Spring源码
        //工厂模式 + 策略模式
        GPBeanPostProcessor postProcessor = new GPBeanPostProcessor();

        postProcessor.postProcessBeforeInitialization(beanInstance, beanName);

        // 将beanWrapper存放到IOC容器中
        this.factoryBeanInstanceCache.put(beanName, beanWrapper);

        // 注入
        populateBean(beanName, beanDefinition, beanWrapper);

        return (T) this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
    }

    // 创建Bean实例对象
    //protected Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
    // 真正创建Bean的方法
    //protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final @Nullable Object[] args)

    protected void populateBean(String beanName, GPBeanDefinition bd, @Nullable GPBeanWrapper bw) {
        if (null == bw) {
            return;
        }
        // BeanPostProcessor

        Object instance = bw.getWrappedInstance();

        Class<?> clazz = bw.getWrappedClass();
        //判断只有加了注解的类，才执行依赖注入
        if (!(clazz.isAnnotationPresent(GPController.class) || clazz.isAnnotationPresent(GPService.class))) {
            return;
        }

        //获得所有的fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(GPAutowired.class)) {
                continue;
            }
            GPAutowired autowired = field.getAnnotation(GPAutowired.class);

            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }

            //强制访问
            field.setAccessible(true);
            try {
                //为什么会为NULL，先留个坑
                if (this.factoryBeanInstanceCache.get(autowiredBeanName) == null) {
                    continue;
                }
//                if(instance == null){
//                    continue;
//                }
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Instantiate the given bean using its default constructor
     * 使用默认的无参构造方法实例化Bean对象
     */
    private GPBeanWrapper instantiateBean(String beanName, GPBeanDefinition beanDefinition) {
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

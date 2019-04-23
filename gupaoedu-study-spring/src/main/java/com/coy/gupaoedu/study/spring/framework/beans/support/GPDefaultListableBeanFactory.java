package com.coy.gupaoedu.study.spring.framework.beans.support;

import com.coy.gupaoedu.study.spring.framework.beans.GPAware;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanDefinition;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactoryAware;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanNameAware;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanWrapper;
import com.coy.gupaoedu.study.spring.framework.beans.GPDisposableBean;
import com.coy.gupaoedu.study.spring.framework.beans.GPInitializingBean;
import com.coy.gupaoedu.study.spring.framework.beans.ObjectFactory;
import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeansException;
import com.coy.gupaoedu.study.spring.framework.beans.factory.config.GPBeanPostProcessor;
import com.coy.gupaoedu.study.spring.framework.beans.factory.config.GPInstantiationAwareBeanPostProcessor;
import com.coy.gupaoedu.study.spring.framework.beans.factory.config.GPScope;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;
import com.coy.gupaoedu.study.spring.framework.core.util.StringUtils;
import com.coy.gupaoedu.study.spring.framework.mvc.annotation.GPAutowired;
import com.coy.gupaoedu.study.spring.framework.mvc.annotation.GPController;
import com.coy.gupaoedu.study.spring.framework.mvc.annotation.GPService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    /**
     * BeanPostProcessors to apply in createBean
     */
    private final List<GPBeanPostProcessor> beanPostProcessors = new ArrayList<>();

    /**
     * Map from scope identifier String to corresponding Scope
     */
    private final Map<String, GPScope> scopes = new LinkedHashMap<>(8);


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
    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public void registerBeanDefinition(String beanName, GPBeanDefinition beanDefinition) {
        if (this.beanDefinitionMap.containsKey(beanName)) {
            throw new GPBeansException(beanName + " BeanDefinition is already exists");
        }
        // 此处简单是实现，暂不考虑多线程的情况
        this.beanDefinitionMap.put(beanName, beanDefinition);
        this.beanDefinitionNames.add(beanName);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        return getBeanNamesForType(type, true);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons) {
        List<String> resolvedBeanNames = new ArrayList<>();
        // 检查所有的BeanDefinition
        for (String beanName : this.beanDefinitionNames) {
            GPBeanDefinition bd = beanDefinitionMap.get(beanName);
            if (null == bd) {
                continue;
            }
            // 检查bean定义是否完整
            if (!bd.isAbstractFlag() && (bd.hasBeanClass() || !bd.isLazyInit())) {
                boolean matchFound = true;
                // 检查beanName是否与type匹配
                if (!isTypeMatch(beanName, type)) {
                    matchFound = false;
                }
                if (bd.isLazyInit() && !containsSingleton(beanName)) {
                    matchFound = false;
                }
                if (!includeNonSingletons && !bd.isSingleton()) {
                    matchFound = false;
                }
                if (matchFound) {
                    resolvedBeanNames.add(beanName);
                }
            }
        }
        return resolvedBeanNames.toArray(new String[resolvedBeanNames.size()]);
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

    @Override
    public void addBeanPostProcessor(GPBeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }

    /**
     * Return the list of BeanPostProcessors that will get applied
     * to beans created with this factory.
     */
    public List<GPBeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    /**
     * 删除单例对象
     */
    @Override
    protected void removeSingleton(String beanName) {
        super.removeSingleton(beanName);
        this.factoryBeanInstanceCache.remove(beanName);
    }

    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName, null, null, false);
    }

    @Override
    public <T> T getBean(Class<T> beanClazz) {
        return (T) this.getBean(beanClazz.getName());
    }

    @Override
    public <T> T getBean(String beanName, Class<T> requiredType) {
        return doGetBean(beanName, requiredType, null, false);
    }

    @Override
    public boolean containsBean(String name) {
        if (containsSingleton(name) || containsBeanDefinition(name)) {
            return true;
        }
        return false;
    }

    /**
     * Return an instance, which may be shared or independent, of the specified bean
     * 真正实现向IOC容器获取Bean的功能，也是触发依赖注入功能的地方
     */
    protected <T> T doGetBean(final String beanName, Class<T> requiredType, final Object[] args, boolean typeCheckOnly) {

        // 获取BeanDefinition
        final GPBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);

        Object bean = null;

        // 创建单例模式Bean的实例对象
        if (beanDefinition.isSingleton()) {
            // 获取单利bean
            // 这里使用了一个匿名内部类，创建Bean实例对象，并且注册给所依赖的对象
            Object singletonInstance = super.getSingleton(beanName, new ObjectFactory<T>() {
                @Override
                public T getObject() throws GPBeansException {
                    try {
                        return (T) createBean(beanName, beanDefinition, args);
                    } catch (GPBeansException ex) {
                        removeSingleton(beanName);
                        throw ex;
                    }
                }
            });
            bean = singletonInstance;
        }
        // IOC容器创建原型模式Bean实例对象
        else if (beanDefinition.isPrototype()) {
            // 原型模式(Prototype)是每次都会创建一个新的对象
            // 创建指定Bean对象实例
            Object prototypeInstance = createBean(beanName, beanDefinition, args);
            bean = prototypeInstance;
        }
        // 要创建的Bean既不是单例模式，也不是原型模式，则根据Bean定义资源中
        // 配置的生命周期范围，选择实例化Bean的合适方法，这种在Web应用程序中
        // 比较常用，如：request、session、application等生命周期
        else {
            String scopeName = beanDefinition.getScope();
            final GPScope scope = this.scopes.get(scopeName);
            // Bean定义资源中没有配置生命周期范围，则Bean定义不合法
            if (scope == null) {
                throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
            }
            try {
                // 这里又使用了一个匿名内部类，获取一个指定生命周期范围的实例
                Object scopedInstance = scope.get(beanName, () -> {
                    return createBean(beanName, beanDefinition, args);
                });
                bean = scopedInstance;
            } catch (IllegalStateException ex) {
                throw new GPBeansException("beanName=" + beanName + "Scope '" + scopeName + "' is not active for the current thread; consider " +
                        "defining a scoped proxy for this bean if you intend to refer to it from a singleton", ex);
            }
        }

        // 对创建的Bean实例对象进行类型检查
        if (null != requiredType && !requiredType.isInstance(bean)) {
            throw new GPBeansException("Failed to convert bean '" + beanName + "' to required type '" + requiredType.getTypeName() + "'");
        }
        return (T) bean;
    }

    /**
     * 创建bean对象实例
     * 注：如果有识别到AOP的拦截器，则创建代理对象
     */
    @Override
    public Object createBean(String beanName, GPBeanDefinition bd, Object[] args) {
        try {
            // 如果Bean配置了初始化前和初始化后的处理器(BeanPostProcessor)，则试图创建一个Bean的代理对象并返回
            Object bean = resolveBeforeInstantiation(beanName, bd);
            if (bean != null) {
                System.out.println("Finished creating proxy instance of bean '" + beanName + "'");
                return bean;
            }
        } catch (Exception ex) {
            throw new GPBeansException("BeanPostProcessor before instantiation of bean failed", ex);
        }

        try {
            // 创建Bean的入口
            Object beanInstance = doCreateBean(beanName, bd, args);
            System.out.println("Finished creating instance of bean '" + beanName + "'");
            return beanInstance;
        } catch (Exception ex) {
            throw new GPBeansException("creating instance of bean failed", ex);
        }
    }

    @Override
    public boolean isSingleton(String beanName) {
        Object beanInstance = getSingleton(beanName);
        if (beanInstance != null) {
            return true;
        }

        GPBeanDefinition mbd = getBeanDefinition(beanName);
        if (mbd.isSingleton()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isPrototype(String beanName) {
        GPBeanDefinition mbd = getBeanDefinition(beanName);
        if (mbd.isPrototype()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isTypeMatch(String beanName, Class<?> typeToMatch) {
        Assert.notNull(typeToMatch, "Class typeToMatch must not be null");
        // 检查注册的单例
        Object beanInstance = getSingleton(beanName);
        if (null != beanInstance) {
            if (typeToMatch.isInstance(beanInstance)) {
                // Direct match for exposed instance?
                return true;
            }
            return false;
        } else if (containsSingleton(beanName) && !containsBeanDefinition(beanName)) {
            // null instance registered
            return false;
        }

        // 单例不存在时，则检查bean定义
        GPBeanDefinition bd = this.beanDefinitionMap.get(beanName);
        return typeToMatch.isAssignableFrom(bd.getBeanClazz());
    }

    @Override
    public Class<?> getType(String beanName) {
        Object beanInstance = getSingleton(beanName);
        if (null != beanInstance) {
            return beanInstance.getClass();
        }

        GPBeanDefinition mbd = getBeanDefinition(beanName);
        if (null != mbd) {
            return mbd.getBeanClazz();
        }
        return null;
    }

    /**
     * 实例化前的解析
     * 如果Bean配置了实例化前和初始化后的处理器(BeanPostProcessor)，则试图创建一个Bean的代理对象并返回
     */
    protected Object resolveBeforeInstantiation(String beanName, GPBeanDefinition bd) {
        Object bean = null;
        // 实例化前的后置处理器
        if (null != bd.getBeanClass()) {
            bean = applyBeanPostProcessorsBeforeInstantiation(bd.getBeanClazz(), beanName);
        }
        // 初始化后的后置处理器
        if (null != bean) {
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }
        return bean;
    }

    /**
     * 实例化前的后置处理器处理
     */
    protected Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
        // 遍历容器为所创建的Bean添加的所有BeanPostProcessor后置处理器
        for (GPBeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof GPInstantiationAwareBeanPostProcessor) {
                GPInstantiationAwareBeanPostProcessor ibp = (GPInstantiationAwareBeanPostProcessor) bp;
                Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * 初始化前的后置处理器处理
     */
    protected Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) {
        Object result = bean;
        // 遍历容器为所创建的Bean添加的所有BeanPostProcessor后置处理器
        for (GPBeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            // 调用Bean实例所有的后置处理中的初始化前处理方法，为Bean实例对象在 初始化之前做一些自定义的处理操作
            Object current = beanProcessor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * 初始化后的后置处理器处理
     */
    protected Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName) {
        Object result = bean;
        // 遍历容器为所创建的Bean添加的所有BeanPostProcessor后置处理器
        for (GPBeanPostProcessor beanProcessor : getBeanPostProcessors()) {
            // 调用Bean实例所有的后置处理中的初始化后处理方法，为Bean实例对象在初始化之后做一些自定义的处理操作

            // AOP Advice的织入就是在此处完成的
            // org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator.postProcessAfterInitialization
            Object current = beanProcessor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * 真正的创建Bean的方法
     */
    protected Object doCreateBean(final String beanName, final GPBeanDefinition bd, final Object[] args) throws Exception {
        // 封装被创建的Bean对象
        GPBeanWrapper instanceWrapper = null;
        if (bd.isSingleton()) {
            // 此处暂时好像没有用，因为没有地方往Map中put值
            instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
        }
        // 创建bean实例并包装
        if (instanceWrapper == null) {
            instanceWrapper = createBeanInstance(beanName, bd, args);
        }
        Object bean = instanceWrapper.getWrappedInstance();

        // TODO 要考虑循环引用的问题
        // 用缓存机制来解决循环依赖的问题，当A引用B，B引用A时，当A注入时，发现B未创建，那么将B未创建这种情况给记录下来，然后再创建B时，进行注入到A即可
        // BeanWarpper

        // 将Bean实例对象封装，并且Bean定义中配置的属性值赋值给实例对象
        populateBean(beanName, bd, instanceWrapper);

        // 初始化bean实例
        Object exposedObject = initializeBean(beanName, bean, bd);

        // 将bean注册为一次性的
        try {
            registerDisposableBeanIfNecessary(beanName, bean, bd);
        } catch (Exception ex) {
            throw new GPBeansException("Error creating bean with name '" + beanName + "' : Invalid destruction signature", ex);
        }
        return exposedObject;
    }

    /**
     * 将给定的bean添加到此工厂的一次性bean列表中
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, GPBeanDefinition bd) {
        if (bd.isSingleton()) {
            if (bean instanceof GPDisposableBean || StringUtils.hasLength(bd.getDestroyMethodName())) {
                // 注册销毁bean
                registerDisposableBean(beanName, new GPDisposableBeanAdapter(bean, beanName, bd));
            }
        }
    }

    /**
     * 创建Bean的实例对象
     */
    protected GPBeanWrapper createBeanInstance(String beanName, GPBeanDefinition bd, Object[] args) {
        try {
            // 使用JDK的反射机制，判断要实例化的Bean是否是接口
            final Class<?> beanClass = bd.getBeanClazz();
            if (beanClass.isInterface()) {
                throw new GPBeansException("Failed to instantiate [" + beanClass.getName() + "]: Specified class is an interface");
            }
            Constructor<?> constructorToUse = beanClass.getDeclaredConstructor();
            constructorToUse.setAccessible(true);
            // 通过反射机制调用”构造方法.newInstance(arg)”来进行实例化
            // 使用默认的无参构造方法实例化Bean对象
            Object beanInstance = constructorToUse.newInstance();

            // 将实例化的对象封装起来
            GPBeanWrapper bw = new GPBeanWrapper(beanInstance);
            return bw;
        } catch (Exception ex) {
            throw new GPBeansException("Instantiation of bean=" + beanName + " failed", ex);
        }
    }

    /**
     * 对Bean实例对象的属性进行赋值，所谓的DI依赖注入
     * 见：org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#populateBean(...)
     */
    protected void populateBean(String beanName, GPBeanDefinition bd, GPBeanWrapper bw) {
        if (null == bw) {
            return;
        }
        Object instance = bw.getWrappedInstance();

        Class<?> clazz = bw.getWrappedClass();
        // 判断只有加了注解的类，才执行依赖注入
        if (!(clazz.isAnnotationPresent(GPController.class) || clazz.isAnnotationPresent(GPService.class))) {
            return;
        }

        // 获得所有的fields
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

            // 判断是否存在bean的单例对象或者bean的定义（即IOC容器里面是否包含有指定名称对应的bean定义）
            if (containsBean(autowiredBeanName)) {

                // 调用getBean方法从IOC容器获取指定名称的Bean实例
                Object bean = getBean(autowiredBeanName);
                try {
                    // 强制访问
                    field.setAccessible(true);
                    field.set(instance, bean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Initialize the given bean instance, applying factory callbacks
     * as well as init methods and bean post processors
     * <p>
     * 初始化Bean实例对象，在初始化方法前后为其执行对应的BeanPostProcessor后置处理器
     */
    protected Object initializeBean(final String beanName, final Object bean, GPBeanDefinition bd) throws Exception {
        Object wrappedBean = bean;
        // 为Bean实例对象包装相关属性，如名称，类加载器，所属容器等信息
        invokeAwareMethods(beanName, bean);

        // Bean实例初始化之前做一些处理(BeanPostProcessor.postProcessBeforeInitialization())
        if (null == bd) {
            wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
        }

        // Bean实例初始化
        // 调用Bean实例对象的初始化方法，这个初始化方法是在Spring Bean定义配置文件中通过init-method属性指定的
        invokeInitMethods(beanName, wrappedBean, bd);

        // Bean实例初始化之后做一些处理(BeanPostProcessor.postProcessAfterInitialization())
        if (null == bd) {
            wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        }
        return wrappedBean;
    }

    /**
     * 执行实现了GPAware包装接口的子类进行：如bean名称、bean工厂
     */
    protected void invokeAwareMethods(String beanName, Object bean) {
        if (bean instanceof GPAware) {
            // 注：因为此处对BeanNameAware和BeanFactoryAware的字段进行了字段赋值，那么在进行DI依赖注入时，需要过滤掉这两个方法对应的属性
            if (bean instanceof GPBeanNameAware) {
                ((GPBeanNameAware) bean).setBeanName(beanName);
            }
            if (bean instanceof GPBeanFactoryAware) {
                ((GPBeanFactoryAware) bean).setBeanFactory(this);
            }
        }
    }

    /**
     * 执行Bean实例对象的初始化方法
     */
    protected void invokeInitMethods(String beanName, Object bean, GPBeanDefinition bd) throws Exception {
        // 针对实现初始化bean的Bean进行初始化处理
        boolean isInitializingBean = (bean instanceof GPInitializingBean);
        if (isInitializingBean) {
            ((GPInitializingBean) bean).afterPropertiesSet();
        }

        // 针对配置了init-method属性的bean进行初始化
        if (null != bd) {
            String initMethodName = bd.getInitMethodName();
            if (StringUtils.hasLength(initMethodName) && !(isInitializingBean && "afterPropertiesSet".equals(initMethodName))) {
                Method initMethod = null;
                initMethod.setAccessible(true);
                initMethod.invoke(bean);
            }
        }
    }

}

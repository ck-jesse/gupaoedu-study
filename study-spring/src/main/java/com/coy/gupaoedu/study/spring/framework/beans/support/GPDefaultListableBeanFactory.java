package com.coy.gupaoedu.study.spring.framework.beans.support;

import com.coy.gupaoedu.study.spring.framework.beans.GPAware;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanDefinition;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactoryAware;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanNameAware;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanWrapper;
import com.coy.gupaoedu.study.spring.framework.beans.GPDisposableBean;
import com.coy.gupaoedu.study.spring.framework.beans.GPFactoryBean;
import com.coy.gupaoedu.study.spring.framework.beans.GPInitializingBean;
import com.coy.gupaoedu.study.spring.framework.beans.ObjectFactory;
import com.coy.gupaoedu.study.spring.framework.beans.exception.BeanIsNotAFactoryException;
import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeanCurrentlyInCreationException;
import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeansException;
import com.coy.gupaoedu.study.spring.framework.beans.exception.GPNoSuchBeanDefinitionException;
import com.coy.gupaoedu.study.spring.framework.beans.factory.BeanFactoryUtils;
import com.coy.gupaoedu.study.spring.framework.beans.factory.config.GPBeanPostProcessor;
import com.coy.gupaoedu.study.spring.framework.beans.factory.config.GPInstantiationAwareBeanPostProcessor;
import com.coy.gupaoedu.study.spring.framework.beans.factory.config.GPScope;
import com.coy.gupaoedu.study.spring.framework.core.NamedThreadLocal;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;
import com.coy.gupaoedu.study.spring.framework.core.util.StringUtils;
import com.coy.gupaoedu.study.spring.framework.beans.annotation.GPAutowired;
import com.coy.gupaoedu.study.spring.framework.context.annotation.GPController;
import com.coy.gupaoedu.study.spring.framework.context.annotation.GPService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenck
 * @date 2019/4/10 21:43
 */
@Slf4j
public class GPDefaultListableBeanFactory extends FactoryBeanRegistrySupport implements GPBeanFactory {

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

    /**
     * Names of beans that are currently in creation
     * 用于记录当前线程是否正在创建prototype类型的bean
     */
    private final ThreadLocal<Object> prototypesCurrentlyInCreation = new NamedThreadLocal<>("Prototype beans currently in creation");

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
        // 含普通的bean和自定义FactoryBean子类，所以此处变向的实现了对于type类型是自定义FactoryBean中的type类型的情况，也就获取到了由自定义FactoryBean创建的bean对象
        for (String beanName : this.beanDefinitionNames) {
            GPBeanDefinition bd = beanDefinitionMap.get(beanName);
            if (null == bd) {
                continue;
            }
            // 检查bean定义是否完整
            if (!bd.isAbstractFlag() && (bd.hasBeanClass() || !bd.isLazyInit())) {
                boolean isFactoryBean = isFactoryBean(beanName, bd);
                boolean matchFound = true;
                // 检查beanName是否与type匹配
                // 注：当beanName对应的bean为FactoryBean时，检查type类型是否为自定义GPFactoryBean中的type类型，
                // 可用于在其他bean中依赖自定义GPFactoryBean中的type类型bean的场景
                if (!isTypeMatch(beanName, type)) {
                    matchFound = false;
                }
                if (bd.isLazyInit() && !containsSingleton(beanName)) {
                    matchFound = false;
                }
                if (!includeNonSingletons && !bd.isSingleton()) {
                    matchFound = false;
                }
                if (!matchFound && isFactoryBean) {
                    // In case of FactoryBean, try to match FactoryBean instance itself next.
                    beanName = FACTORY_BEAN_PREFIX + beanName;
                    matchFound = (includeNonSingletons || bd.isSingleton()) && isTypeMatch(beanName, type);
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
                // 如果指定名称的bean是创建bean的FactoryBean
                if (isFactoryBean(beanName)) {
                    // FACTORY_BEAN_PREFIX=”&”，当Bean名称前面加”&”符号时，获取的是产生容器对象本身，而不是容器产生的Bean.
                    // 调用getBean方法，触发容器对Bean实例化和依赖注入过程
                    final GPFactoryBean<?> factory = (GPFactoryBean<?>) getBean(FACTORY_BEAN_PREFIX + beanName);


                } else {
                    this.getBean(beanName);
                }
            }
        }
        // TODO 触发所有适用bean的初始化后回调
    }

    /**
     * 判断是否为FactoryBean
     */
    private boolean isFactoryBean(String name) {
        String beanName = transformedBeanName(name);

        Object beanInstance = getSingleton(beanName, false);
        if (beanInstance != null) {
            return (beanInstance instanceof GPFactoryBean);
        }

        GPBeanDefinition bd = this.beanDefinitionMap.get(beanName);
        return isFactoryBean(beanName, bd);
    }

    /**
     * Check whether the given bean is defined as a {@link GPFactoryBean}.
     *
     * @param beanName the name of the bean
     * @param bd       bd corresponding bean definition
     */
    protected boolean isFactoryBean(String beanName, GPBeanDefinition bd) {
        Class<?> beanType = predictBeanType(beanName, bd, GPFactoryBean.class);
        return (beanType != null && GPFactoryBean.class.isAssignableFrom(beanType));
    }

    /**
     * 预测bean类型
     */
    protected Class<?> predictBeanType(String beanName, GPBeanDefinition mbd, Class<?>... typesToMatch) {
        Class<?> targetType = mbd.getBeanClazz();
        if (targetType != null) {
            return targetType;
        }
        //return resolveBeanClass(mbd, beanName, typesToMatch);
        return null;
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
    protected <T> T doGetBean(final String name, Class<T> requiredType, final Object[] args, boolean typeCheckOnly) {
        // 去掉FactoryBean的前缀，返回bean名称
        String beanName = transformedBeanName(name);

        Object bean = null;
        // 从容器中获取单例bean，若存在，则无需重复创建（此处可获取到已经创建但还未初始化完全的单例bean（若匹配aop规则，则会创建对应的代理对象并返回））
        Object sharedInstance = getSingleton(beanName);
        if (null != sharedInstance && args == null) {
            // bean = sharedInstance;
            // 获取给定Bean的实例对象，主要是完成FactoryBean的相关处理
            // 注意：BeanFactory是管理容器中Bean的工厂，而FactoryBean是创建对象的工厂Bean，两者之间有区别
            bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);

            // 对创建的Bean实例对象进行类型检查
            if (null != requiredType && !requiredType.isInstance(bean)) {
                throw new GPBeansException("Failed to convert bean '" + beanName + "' to required type '" + requiredType.getTypeName() + "'");
            }
            return (T) bean;
        }

        // 判断缓存中是否存在正在创建的prototype bean，若存在，则表示存在循环引用的问题导致实例化对象十遍
        if (isPrototypeCurrentlyInCreation(beanName)) {
            throw new GPBeanCurrentlyInCreationException(beanName);
        }

        // 获取BeanDefinition
        final GPBeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            this.logger.warn("No bean named '" + beanName + "' found in " + this);
            throw new GPNoSuchBeanDefinitionException(beanName);
        }
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
            // bean = singletonInstance;
            bean = getObjectForBeanInstance(singletonInstance, name, beanName, beanDefinition);
        }
        // IOC容器创建原型模式Bean实例对象
        else if (beanDefinition.isPrototype()) {
            // 原型模式(Prototype)是每次都会创建一个新的对象
            Object prototypeInstance = null;
            try {
                // 回调beforePrototypeCreation方法，默认的功能是注册当前创建的原型对象
                beforePrototypeCreation(beanName);
                // 创建指定Bean对象实例
                prototypeInstance = createBean(beanName, beanDefinition, args);
            } finally {
                // 回调afterPrototypeCreation方法，默认告诉IOC容器指定Bean的原型对象不再创建
                afterPrototypeCreation(beanName);
            }
            // bean = prototypeInstance;
            bean = getObjectForBeanInstance(prototypeInstance, name, beanName, beanDefinition);
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
                // bean = scopedInstance;
                bean = getObjectForBeanInstance(scopedInstance, name, beanName, beanDefinition);
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
     * 获取给定Bean的实例对象，主要是完成FactoryBean的相关处理
     */
    public Object getObjectForBeanInstance(Object beanInstance, String name, String beanName, GPBeanDefinition bd) {
        // 如果beanName是&开头，且bean不是FactoryBean，因约定&开头的是FactoryBean，所以当bean不是FactoryBean时，则抛出异常
        // 实则为限制&开头的beanName，则bean必须是FactoryBean
        if (BeanFactoryUtils.isFactoryDereference(name) && !(beanInstance instanceof GPFactoryBean)) {
            throw new BeanIsNotAFactoryException(transformedBeanName(name), beanInstance.getClass());
        }

        // 当bean不是FacatoryBean 或者 beanName不是&开头，则直接返回bean
        if (!(beanInstance instanceof GPFactoryBean) || BeanFactoryUtils.isFactoryDereference(name)) {
            return beanInstance;
        }

        // 处理指定名称不是容器的解引用，或者根据名称获取的Bean实例对象是一个工厂Bean
        // 使用工厂Bean创建一个Bean的实例对象
        Object object = null;
        if (bd == null) {
            // 从Bean工厂缓存中获取给定名称的Bean实例对象
            object = getCachedObjectForFactoryBean(beanName);
        }
        // 让Bean工厂生产给定名称的Bean对象实例
        if (object == null) {
            // Return bean instance from factory.
            GPFactoryBean<?> factory = (GPFactoryBean<?>) beanInstance;
            // 如果从容器得到Bean定义信息，并且Bean定义信息不是虚构的，
            // 则让工厂Bean生产Bean实例对象
            boolean synthetic = (bd != null && bd.isSynthetic());
            // 调用FactoryBeanRegistrySupport类的getObjectFromFactoryBean方法，
            // 实现工厂Bean生产Bean对象实例的过程
            object = getObjectFromFactoryBean(factory, beanName, !synthetic);
        }
        return object;
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
        String beanName1 = transformedBeanName(beanName);
        Object beanInstance = getSingleton(beanName1, false);
        if (beanInstance != null) {
            if (beanInstance instanceof GPFactoryBean) {
                return (BeanFactoryUtils.isFactoryDereference(beanName) || ((GPFactoryBean<?>) beanInstance).isSingleton());
            }
            return !BeanFactoryUtils.isFactoryDereference(beanName);
        }

        GPBeanDefinition mbd = getBeanDefinition(beanName1);
        if (mbd.isSingleton()) {
            if (isFactoryBean(beanName, mbd)) {
                if (BeanFactoryUtils.isFactoryDereference(beanName)) {
                    return true;
                }
                GPFactoryBean<?> factoryBean = (GPFactoryBean<?>) getBean(FACTORY_BEAN_PREFIX + beanName);
                return factoryBean.isSingleton();
            } else {
                return !BeanFactoryUtils.isFactoryDereference(beanName);
            }
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
    public boolean isTypeMatch(String name, Class<?> typeToMatch) {
        Assert.notNull(typeToMatch, "Class typeToMatch must not be null");
        String beanName = transformedBeanName(name);
        // 检查注册的单例
        Object beanInstance = getSingleton(beanName, false);
        if (null != beanInstance) {
            // 当bean为FactoryBean时，检查typeToMatch类型是否为自定义了GPFactoryBean中的type类型
            if (beanInstance instanceof GPFactoryBean) {
                if (!BeanFactoryUtils.isFactoryDereference(name)) {
                    // 从FactoryBean之类中获取type类型
                    Class<?> type = getTypeForFactoryBean((GPFactoryBean<?>) beanInstance);
                    // 此处判断传入的typeToMatch类型与FactoryBean中的type类型是否一致，若一致的话，则认为是自定义了GPFactoryBean来创建typeToMatch类型对应的类
                    return (type != null && typeToMatch.isAssignableFrom(type));
                } else {
                    return typeToMatch.isInstance(beanInstance);
                }
            }
            if (typeToMatch.isInstance(beanInstance)) {
                // Direct match for exposed instance?
                return true;
            }
            return false;
        } else if (containsSingleton(beanName) && !containsBeanDefinition(beanName)) {
            // null instance registered
            return false;
        }

        // 预测

        // 单例不存在时，则检查bean定义
        GPBeanDefinition bd = this.beanDefinitionMap.get(beanName);
        return typeToMatch.isAssignableFrom(bd.getBeanClazz());
    }

    @Override
    public Class<?> getType(String beanName) {
        Object beanInstance = getSingleton(beanName, false);
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
     * 去掉FactoryBean的前缀，返回bean名称
     * Return the bean name, stripping out the factory dereference prefix if necessary,
     * and resolving aliases to canonical names.
     *
     * @param name the user-specified name
     * @return the transformed bean name
     */
    protected String transformedBeanName(String name) {
        return BeanFactoryUtils.transformedBeanName(name);
    }

    /**
     * 决定bean名称，将本地定义的别名解析为规范名称
     * Determine the original bean name, resolving locally defined aliases to canonical names.
     *
     * @param name the user-specified name
     * @return the original bean name
     */
    protected String originalBeanName(String name) {
        String beanName = transformedBeanName(name);
        if (name.startsWith(FACTORY_BEAN_PREFIX)) {
            beanName = FACTORY_BEAN_PREFIX + beanName;
        }
        return beanName;
    }


    /**
     * 判断原型bean当前是否在创建中
     * Return whether the specified prototype bean is currently in creation
     * (within the current thread).
     *
     * @param beanName the name of the bean
     */
    protected boolean isPrototypeCurrentlyInCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        return (curVal != null &&
                (curVal.equals(beanName) || (curVal instanceof Set && ((Set<?>) curVal).contains(beanName))));
    }

    /**
     * 在创建原型bean之前回调。默认将原型bean注册到当前正在创建中的容器.
     * Callback before prototype creation.
     * <p>The default implementation register the prototype as currently in creation.
     *
     * @param beanName the name of the prototype about to be created
     * @see #isPrototypeCurrentlyInCreation
     */
    protected void beforePrototypeCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        if (curVal == null) {
            this.prototypesCurrentlyInCreation.set(beanName);
        } else if (curVal instanceof String) {
            Set<String> beanNameSet = new HashSet<>(2);
            beanNameSet.add((String) curVal);
            beanNameSet.add(beanName);
            this.prototypesCurrentlyInCreation.set(beanNameSet);
        } else {
            Set<String> beanNameSet = (Set<String>) curVal;
            beanNameSet.add(beanName);
        }
    }

    /**
     * 在创建原型bean之后回调。默认将原型bean从创建中的容器中移除.
     * Callback after prototype creation.
     * <p>The default implementation marks the prototype as not in creation anymore.
     *
     * @param beanName the name of the prototype that has been created
     * @see #isPrototypeCurrentlyInCreation
     */
    protected void afterPrototypeCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        if (curVal instanceof String) {
            this.prototypesCurrentlyInCreation.remove();
        } else if (curVal instanceof Set) {
            Set<String> beanNameSet = (Set<String>) curVal;
            beanNameSet.remove(beanName);
            if (beanNameSet.isEmpty()) {
                this.prototypesCurrentlyInCreation.remove();
            }
        }
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
        if (null == instanceWrapper) {
            // 基于构造函数来实例化bean
            instanceWrapper = createBeanInstance(beanName, bd, args);
        }
        Object bean = instanceWrapper.getWrappedInstance();

        // 用缓存机制来解决循环依赖的问题，当A引用B，B引用A时，当A注入时，发现B未创建，那么将B未创建这种情况给记录下来，然后再创建B时，进行注入到A即可
        // 向提前暴露容器中缓存单例Bean对象，以防循环引用
        // 注意：因为bean是通过构造函数来进行实例化，所以针对构造函数的循环依赖没法解决
        boolean earlySingletonExposure = (bd.isSingleton() && isSingletonCurrentlyInCreation(beanName));
        if (earlySingletonExposure) {
            log.debug("Eagerly caching bean '" + beanName + "' to allow for resolving potential circular references");
            // 缓存单例bean的引用（以单例工厂为基础）
            // 目的为提前暴露单例bean的引用，为了防止循环引用，以便尽早持有对象的引用
            addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, bd, bean));
        }

        // 将Bean实例对象封装，并且Bean定义中配置的属性值赋值给实例对象
        populateBean(beanName, bd, instanceWrapper);

        // 初始化bean实例
        Object exposedObject = initializeBean(beanName, bean, bd);

        if (earlySingletonExposure) {
            // 先从单例bean容器singletonObjects中获取bean，不存在，再从bean提前暴露容器earlySingletonObjects中获取bean，还不存在，则从提前暴露的单例bean工厂singletonFactories获取bean
            Object earlySingletonReference = getSingleton(beanName, false);
            if (earlySingletonReference != null) {
                // 根据名称获取的已注册的Bean和正在实例化的Bean是同一个
                if (exposedObject == bean) {
                    // 当前实例化的Bean初始化完成
                    exposedObject = earlySingletonReference;
                }
            }
        }

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
            if (Modifier.isAbstract(beanClass.getModifiers())) {
                throw new GPBeansException("Failed to instantiate [" + beanClass.getName() + "]: Specified class is an abstract class");
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
     * 获取对指定bean的早期访问的引用，通常用于解析循环引用
     * <p>
     * Obtain a reference for early access to the specified bean,
     * typically for the purpose of resolving a circular reference.
     *
     * @param beanName the name of the bean (for error handling purposes)
     * @param bd       the merged bean definition for the bean
     * @param bean     the raw bean instance
     * @return the object to expose as bean reference
     */
    protected Object getEarlyBeanReference(String beanName, GPBeanDefinition bd, Object bean) {
        Object exposedObject = bean;
        for (GPBeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof GPInstantiationAwareBeanPostProcessor) {
                GPInstantiationAwareBeanPostProcessor ibp = (GPInstantiationAwareBeanPostProcessor) bp;
                // 获取对指定bean的早期访问的引用(此处实际为获取代理对象)
                exposedObject = ibp.getEarlyBeanReference(exposedObject, beanName);
            }
        }
        return exposedObject;
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

        try {
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

                // 根据名称对属性进行自动依赖注入
                if (autowireByName(instance, field, autowiredBeanName)) {
                    continue;
                }

                // 根据类型对属性进行自动依赖注入
                autowireByType(instance, field);
            }
        } catch (Exception ex) {
            throw new GPBeansException("Instantiation of bean=" + beanName + " failed", ex);
        }
    }

    /**
     * 根据名称对属性进行自动依赖注入
     */
    protected boolean autowireByName(Object instance, Field field, String autowiredBeanName) throws IllegalAccessException {
        // 判断是否存在bean的单例对象或者bean的定义（即IOC容器里面是否包含有指定名称对应的bean定义）
        if (containsBean(autowiredBeanName)) {
            // 调用getBean方法从IOC容器获取指定名称的Bean实例
            Object beanInstance = getBean(autowiredBeanName);
            // 强制访问
            field.setAccessible(true);
            field.set(instance, beanInstance);
            return true;
        }
        return false;
    }

    /**
     * 根据类型对属性进行自动依赖注入
     */
    protected void autowireByType(Object instance, Field field) throws IllegalAccessException {
        if (Object.class != field.getType()) {
            // 根据字段的class类型获取候选bean名称
            // 此处可获取自定义FactoryBean的beanName，供下面getBean(beanName)来获取自定义FactoryBean创建bean
            String[] candidateNames = getBeanNamesForType(field.getType(), true);
            if (null == candidateNames || candidateNames.length == 0) {
                throw new GPNoSuchBeanDefinitionException(field.getType(),
                        "expected at least 1 bean which qualifies as autowire candidate.");
            }
            if (candidateNames.length > 1) {
                throw new GPBeansException("more than one bean found among candidates.");
            }
            Object beanInstance = getBean(candidateNames[0]);
            // 强制访问
            field.setAccessible(true);
            field.set(instance, beanInstance);
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
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);

        // Bean实例初始化
        // 调用Bean实例对象的初始化方法，这个初始化方法是在Spring Bean定义配置文件中通过init-method属性指定的
        invokeInitMethods(beanName, wrappedBean, bd);

        // Bean实例初始化之后做一些处理(BeanPostProcessor.postProcessAfterInitialization())
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
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

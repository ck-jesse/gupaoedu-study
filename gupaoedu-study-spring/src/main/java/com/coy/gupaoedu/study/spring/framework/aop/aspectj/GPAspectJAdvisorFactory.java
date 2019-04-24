package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.GPAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPAfterReturningAdviceInterceptor;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInterceptor;
import com.coy.gupaoedu.study.spring.framework.aop.support.GPDefaultPointcutAdvisor;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.pattern.GPTypePatternMatchingPointcut;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.context.PropertiesUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Advisor工厂
 *
 * @author chenck
 * @date 2019/4/23 15:46
 */
public class GPAspectJAdvisorFactory {

    private GPBeanFactory beanFactory;

    private volatile List<String> aspectBeanNames;

    private final Map<String, List<GPAdvisor>> advisorsCache = new ConcurrentHashMap<>();

//    private final Map<String, MetadataAwareAspectInstanceFactory> aspectFactoryCache = new ConcurrentHashMap<>();

    private List<Pattern> includePatterns;

    public GPAspectJAdvisorFactory(GPBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 确定指定class是否为切面
     * Determine whether or not the given class is an aspect
     *
     * @param clazz the supposed annotation-style AspectJ class
     * @return whether or not this class is recognized by AspectJ as an aspect class
     */
    public boolean isAspect(Class<?> clazz) {

        return false;
    }

    /**
     * Set a list of regex patterns, matching eligible @AspectJ bean names.
     * <p>Default is to consider all @AspectJ beans as eligible.
     */
    public void setIncludePatterns(List<String> patterns) {
        this.includePatterns = new ArrayList<>(patterns.size());
        for (String patternText : patterns) {
            this.includePatterns.add(Pattern.compile(patternText));
        }
    }

    /**
     * 判断是否为合格的切面bean
     * Check whether the given aspect bean is eligible for auto-proxying.
     * <p>If no &lt;aop:include&gt; elements were used then "includePatterns" will be
     * {@code null} and all beans are included. If "includePatterns" is non-null,
     * then one of the patterns must match.
     */
    protected boolean isEligibleAspectBean(String beanName) {
        if (this.includePatterns == null) {
            return true;
        }
        for (Pattern pattern : this.includePatterns) {
            if (pattern.matcher(beanName).matches()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 在当前bean工厂中查找带aspectj注释的方面bean
     * 注：可简单实现为从配置文件加载对应的配置
     * <p>
     * Look for AspectJ-annotated aspect beans in the current bean factory,
     * and return to a list of Spring AOP Advisors representing them.
     * <p>Creates a Spring Advisor for each AspectJ advice method.
     */
    public List<GPAdvisor> buildAspectJAdvisors() {
        List<String> aspectNames = this.aspectBeanNames;

        if (aspectNames == null) {
            synchronized (this) {
                aspectNames = this.aspectBeanNames;
                if (aspectNames == null) {
                    List<GPAdvisor> advisors = new LinkedList<>();
                    aspectNames = new LinkedList<>();
                    String[] beanNames = this.beanFactory.getBeanNamesForType(Object.class, true);
                    for (String beanName : beanNames) {
                        if (!isEligibleAspectBean(beanName)) {
                            continue;
                        }
                        // We must be careful not to instantiate beans eagerly as in this case they
                        // would be cached by the Spring container but would not have been weaved.
                        // 从beanFactory中获取bean的class
                        Class<?> beanType = this.beanFactory.getType(beanName);
                        if (beanType == null) {
                            continue;
                        }
                        // 判断该bean是不是有定义Aspect注解，如果有，则看做一个切面
                        if (this.isAspect(beanType)) {
                            aspectNames.add(beanName);
//                            AspectMetadata amd = new AspectMetadata(beanType, beanName);
//                            if (amd.getAjType().getPerClause().getKind() == PerClauseKind.SINGLETON) {
//                                MetadataAwareAspectInstanceFactory factory =
//                                        new BeanFactoryAspectInstanceFactory(this.beanFactory, beanName);
//                                List<Advisor> classAdvisors = this.advisorFactory.getAdvisors(factory);
//                                if (this.beanFactory.isSingleton(beanName)) {
//                                    this.advisorsCache.put(beanName, classAdvisors);
//                                } else {
//                                    this.aspectFactoryCache.put(beanName, factory);
//                                }
//                                advisors.addAll(classAdvisors);
//                            } else {
//                                // Per target or per this.
//                                if (this.beanFactory.isSingleton(beanName)) {
//                                    throw new IllegalArgumentException("Bean with name '" + beanName +
//                                            "' is a singleton, but aspect instantiation model is not singleton");
//                                }
//                                MetadataAwareAspectInstanceFactory factory =
//                                        new PrototypeAspectInstanceFactory(this.beanFactory, beanName);
//                                this.aspectFactoryCache.put(beanName, factory);
//                                advisors.addAll(this.advisorFactory.getAdvisors(factory));
//                            }
                        }
                    }
                    this.aspectBeanNames = aspectNames;
                    return advisors;
                }
            }
        }

        if (aspectNames.isEmpty()) {
            return Collections.emptyList();
        }
        List<GPAdvisor> advisors = new LinkedList<>();
        for (String aspectName : aspectNames) {
            List<GPAdvisor> cachedAdvisors = this.advisorsCache.get(aspectName);
            if (cachedAdvisors != null) {
                advisors.addAll(cachedAdvisors);
            } else {
//                MetadataAwareAspectInstanceFactory factory = this.aspectFactoryCache.get(aspectName);
//                advisors.addAll(this.advisorFactory.getAdvisors(factory));
            }
        }
        return advisors;
    }

    /**
     * 将切面AspectJ中的方法构建为Advisor
     * 注：spring的此处实现过于复杂，仅参考
     */
    public List<GPAdvisor> myBuildAspectJAdvisors() {

        String aspectPointcut = PropertiesUtils.getAspectPointcut();
        String aspectClass = PropertiesUtils.getAspectClass();

        // 将表达式处理为正则支持的表达式
        aspectPointcut = aspectPointcut
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)");
        // Pattern aspectPointcutPattern = Pattern.compile(aspectPointcut);
        try {
            // aspect切面类
            Class aspectClazz = Class.forName(aspectClass);

            // 将Aspect切面类中的Method包装为Advisor
            List<GPAdvisor> advisorList = getAspectAdvisors(aspectClazz, aspectPointcut);

            // 缓存Aspect切面类的Advisor
            advisorsCache.put(aspectClass, advisorList);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Aspect切面类中的Method包装为Advisor，没一个方法都是一个Advisor
     */
    public List<GPAdvisor> getAspectAdvisors(Class aspectClazz, String aspectPointcut) {
        List<GPAdvisor> advisorList = new ArrayList<>();
        // 将aspect切面类中的Method进行包装
        for (Method method : aspectClazz.getMethods()) {
            GPAdvisor advisor = getAdvisor(method, aspectClazz, aspectPointcut);
            if (advisor != null) {
                advisorList.add(advisor);
            }
        }
        return advisorList;
    }

    /**
     *
     */
    public GPAdvisor getAdvisor(Method candidateAdviceMethod, Class aspectClazz, String aspectPointcut) {
        String aspectBefore = PropertiesUtils.getAspectBefore();
        String aspectAfter = PropertiesUtils.getAspectAfter();
        String aspectAfterThrow = PropertiesUtils.getAspectAfterThrow();

        // 查找method是否为合格的切面方法（也就是与配置文件中配置的方法名相同的方法）
        String methodName = candidateAdviceMethod.getName();
        GPMethodInterceptor methodInterceptor = null;
        if (methodName.equals(aspectBefore)) {

            methodInterceptor = new GPAfterReturningAdviceInterceptor();
        }
        if (methodName.equals(aspectAfter)) {

        }
        if (methodName.equals(aspectAfterThrow)) {
            String aspectAfterThrowingName = PropertiesUtils.getAspectAfterThrowingName();

        }
        // 创建切入点，用于对目标方法进行匹配，看目标方法是否合格
        GPPointcut pointcut = new GPTypePatternMatchingPointcut(aspectPointcut);

        // 切入点和通知的顾问（相当于代理）
        GPAspectJPointcutAdvisor aspectJAdvisor = new GPAspectJPointcutAdvisor();
        GPDefaultPointcutAdvisor advisor = new GPDefaultPointcutAdvisor(pointcut, methodInterceptor);
        return advisor;
    }


}

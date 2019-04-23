package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.GPAdvisor;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;

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
     * 构建
     * 可简单实现
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
                        Class<?> beanType = this.beanFactory.getType(beanName);
                        if (beanType == null) {
                            continue;
                        }
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


}

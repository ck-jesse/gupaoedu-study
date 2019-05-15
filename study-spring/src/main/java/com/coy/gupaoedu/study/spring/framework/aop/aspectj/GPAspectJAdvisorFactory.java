package com.coy.gupaoedu.study.spring.framework.aop.aspectj;

import com.coy.gupaoedu.study.spring.framework.aop.GPAdvisor;
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

    // Map<beanName, 对应的拦截器链>
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
     * 在当前bean工厂中查找带aspectj注释的切面bean，并将切面bean中的方法构建为Advisor
     * 注：暂时简单实现为从配置文件加载对应的配置
     */
    public List<GPAdvisor> buildAspectJAdvisors() {
        List<String> aspectNames = this.aspectBeanNames;

        if (aspectNames == null) {
            synchronized (this) {
                aspectNames = this.aspectBeanNames;
                if (aspectNames == null) {
                    // TODO 目前只定义了一个aspectj配置，后续可改造为多个（建议基于注解的方式来实现，类似spring）
                    String aspectPointcut = PropertiesUtils.getAspectPointcut();
                    String aspectClass = PropertiesUtils.getAspectClass();
                    // 将pointcut表达式转换为正则表达式
                    // 注：默认配置aspectPointcut表达式为全的表达式，所以可以理解为针对method的表达式
                    String aspectPointcutRegx = convertToRegx(aspectPointcut);

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
                        // if (this.isAspect(beanType)) {
                        if (beanType.getName().equals(aspectClass)) {
                            aspectNames.add(beanName);
                            // 将Aspect切面类中的Method包装为Advisor
                            List<GPAdvisor> classAdvisors = getAspectAdvisors(beanType, aspectPointcutRegx);
                            this.advisorsCache.put(beanName, classAdvisors);
                            advisors.addAll(classAdvisors);
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
            }
        }
        return advisors;
    }

    /**
     * 将pointcut表达式转换为正则表达式
     */
    public String convertToRegx(String aspectPointcut) {
        String aspectPointcutRegx = aspectPointcut
                .replaceAll("\\.", "\\\\.")
                .replaceAll("\\\\.\\*", ".*")
                .replaceAll("\\(", "\\\\(")
                .replaceAll("\\)", "\\\\)").trim();
        // 以public开头表达式的处理
        // 因为对于接口的方法为： public abstract java.lang.String com.coy.gupaoedu.study.spring.demo.service.IDemoService.get(java.lang.String)
        if (aspectPointcutRegx.startsWith("public")) {
            aspectPointcutRegx = aspectPointcutRegx.replaceAll("public\\s+\\*\\s+", "public +[\\\\w\\\\s.*]* +");
        }
        // 以*开头表达式的处理
        if (aspectPointcutRegx.startsWith("*")) {
            aspectPointcutRegx = aspectPointcutRegx.replaceAll("\\*\\s+", "[\\\\w\\\\s]* +");
        }
        return aspectPointcutRegx;
    }

    /**
     * 将Aspect切面类中的Method包装为Advisor，没一个方法都是一个Advisor
     */
    public List<GPAdvisor> getAspectAdvisors(Class aspectClazz, String aspectPointcutRegx) {

        List<GPAdvisor> advisorList = new ArrayList<>();
        // 将aspect切面类中的Method进行包装
        for (Method method : aspectClazz.getMethods()) {
            // 将method包装为顾问Advisor
            GPAdvisor advisor = getAdvisor(method, aspectClazz, aspectPointcutRegx);
            if (advisor != null) {
                advisorList.add(advisor);
            }
        }
        return advisorList;
    }

    /**
     * 将method包装为顾问Advisor
     */
    public GPAdvisor getAdvisor(Method candidateAdviceMethod, Class aspectClazz, String aspectPointcutRegx) {
        String aspectBefore = PropertiesUtils.getAspectBefore();
        String aspectAfter = PropertiesUtils.getAspectAfter();
        String aspectAfterThrow = PropertiesUtils.getAspectAfterThrow();

        // 创建切入点，用于对目标方法进行匹配，看目标方法是否合格
        GPPointcut pointcut = new GPTypePatternMatchingPointcut(aspectPointcutRegx);

        // 查找method是否为合格的切面方法（也就是与配置文件中配置的方法名相同的方法）
        String methodName = candidateAdviceMethod.getName();
        GPAbstractAspectJAdvice advice = null;
        if (methodName.equals(aspectBefore)) {
            advice = new GPAspectJMethodBeforeAdvice(candidateAdviceMethod, pointcut, this.beanFactory);
        }
        if (methodName.equals(aspectAfter)) {
            advice = new GPAspectJAfterReturningAdvice(candidateAdviceMethod, pointcut, this.beanFactory);
        }
        if (methodName.equals(aspectAfterThrow)) {
            String aspectAfterThrowingName = PropertiesUtils.getAspectAfterThrowingName();
            advice = new GPAspectJAfterThrowingAdvice(candidateAdviceMethod, pointcut, this.beanFactory, aspectAfterThrowingName);
        }
        if (null == advice) {
            return null;
        }
        advice.setAspectName(aspectClazz.getName());
        // 切入点和通知的顾问（相当于代理）
        GPAspectJPointcutAdvisor aspectJAdvisor = new GPAspectJPointcutAdvisor(advice);
        return aspectJAdvisor;
    }


}

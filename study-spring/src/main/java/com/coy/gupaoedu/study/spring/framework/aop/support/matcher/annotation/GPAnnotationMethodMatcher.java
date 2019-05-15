package com.coy.gupaoedu.study.spring.framework.aop.support.matcher.annotation;

import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPMethodMatcher;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;
import com.sun.istack.internal.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author chenck
 * @date 2019/4/23 20:53
 */
public class GPAnnotationMethodMatcher implements GPMethodMatcher {

    private final Class<? extends Annotation> annotationType;

    private final boolean checkInherited;

    public GPAnnotationMethodMatcher(Class<? extends Annotation> annotationType) {
        this(annotationType, false);
    }

    public GPAnnotationMethodMatcher(Class<? extends Annotation> annotationType, boolean checkInherited) {
        Assert.notNull(annotationType, "Annotation type must not be null");
        this.annotationType = annotationType;
        this.checkInherited = checkInherited;
    }

    @Override
    public boolean matches(Method method, @Nullable Class<?> targetClass) {
        if (matchesMethod(method)) {
            return true;
        }
        return false;
//        // The method may be on an interface, so let's check on the target class as well.
//        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
//        return (specificMethod != method && matchesMethod(specificMethod));
    }

    private boolean matchesMethod(Method method) {
        return false;
//        return (this.checkInherited ?
//                (AnnotationUtils.findAnnotation(method, this.annotationType) != null) :
//                method.isAnnotationPresent(this.annotationType));
    }

    @Override
    public boolean isRuntime() {
        return false;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        return false;
    }
}

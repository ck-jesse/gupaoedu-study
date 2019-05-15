package com.coy.gupaoedu.study.spring.framework.aop.support.matcher.annotation;

import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPClassFilter;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPMethodMatcher;
import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPPointcut;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;
import com.sun.istack.internal.Nullable;

import java.lang.annotation.Annotation;

/**
 * @author chenck
 * @date 2019/4/23 20:57
 */
public class GPAnnotationMatchingPointcut implements GPPointcut {

    private final GPClassFilter classFilter;

    private final GPMethodMatcher methodMatcher;


    /**
     * Create a new AnnotationMatchingPointcut for the given annotation type.
     *
     * @param classAnnotationType the annotation type to look for at the class level
     */
    public GPAnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType) {
        this(classAnnotationType, false);
    }

    public GPAnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType, boolean checkInherited) {
        this.classFilter = new GPAnnotationClassFilter(classAnnotationType, checkInherited);
        this.methodMatcher = GPMethodMatcher.TRUE;
    }

    public GPAnnotationMatchingPointcut(Class<? extends Annotation> classAnnotationType,
                                        Class<? extends Annotation> methodAnnotationType) {
        this(classAnnotationType, methodAnnotationType, false);
    }

    public GPAnnotationMatchingPointcut(@Nullable Class<? extends Annotation> classAnnotationType,
                                        @Nullable Class<? extends Annotation> methodAnnotationType, boolean checkInherited) {

        Assert.isTrue((classAnnotationType != null || methodAnnotationType != null),
                "Either Class annotation type or Method annotation type needs to be specified (or both)");

        if (classAnnotationType != null) {
            this.classFilter = new GPAnnotationClassFilter(classAnnotationType, checkInherited);
        } else {
            this.classFilter = GPClassFilter.TRUE;
        }

        if (methodAnnotationType != null) {
            this.methodMatcher = new GPAnnotationMethodMatcher(methodAnnotationType, checkInherited);
        } else {
            this.methodMatcher = GPMethodMatcher.TRUE;
        }
    }

    @Override
    public GPClassFilter getClassFilter() {
        return this.classFilter;
    }

    @Override
    public GPMethodMatcher getMethodMatcher() {
        return this.methodMatcher;
    }
}

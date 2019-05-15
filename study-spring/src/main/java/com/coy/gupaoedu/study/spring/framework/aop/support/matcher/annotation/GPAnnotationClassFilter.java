package com.coy.gupaoedu.study.spring.framework.aop.support.matcher.annotation;

import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPClassFilter;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;

import java.lang.annotation.Annotation;

/**
 * 基于注解的class过滤
 * @author chenck
 * @date 2019/4/23 20:51
 */
public class GPAnnotationClassFilter implements GPClassFilter {

    private final Class<? extends Annotation> annotationType;

    private final boolean checkInherited;

    public GPAnnotationClassFilter(Class<? extends Annotation> annotationType) {
        this(annotationType, false);
    }

    public GPAnnotationClassFilter(Class<? extends Annotation> annotationType, boolean checkInherited) {
        Assert.notNull(annotationType, "Annotation type must not be null");
        this.annotationType = annotationType;
        this.checkInherited = checkInherited;
    }

    @Override
    public boolean matches(Class<?> clazz) {
//        return (this.checkInherited ?
//                (AnnotationUtils.findAnnotation(clazz, this.annotationType) != null) :
//                clazz.isAnnotationPresent(this.annotationType));
        return false;
    }

}

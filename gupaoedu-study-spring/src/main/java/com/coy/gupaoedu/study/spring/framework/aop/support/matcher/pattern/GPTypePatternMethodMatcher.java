package com.coy.gupaoedu.study.spring.framework.aop.support.matcher.pattern;

import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPMethodMatcher;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 正则表达式匹配类
 *
 * @author chenck
 * @date 2019/4/23 21:02
 */
public class GPTypePatternMethodMatcher implements GPMethodMatcher {

    private String typePattern = "";

    private Pattern pattern;

    public GPTypePatternMethodMatcher() {
    }

    public GPTypePatternMethodMatcher(String typePattern) {
        setTypePattern(typePattern);
    }

    public String getTypePattern() {
        return typePattern;
    }

    public void setTypePattern(String typePattern) {
        this.typePattern = typePattern;
        pattern = Pattern.compile(typePattern);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return this.pattern.matcher(method.getName()).matches();
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

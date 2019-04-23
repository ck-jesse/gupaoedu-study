package com.coy.gupaoedu.study.spring.framework.aop.support.matcher.pattern;

import com.coy.gupaoedu.study.spring.framework.aop.support.matcher.GPClassFilter;

import java.util.regex.Pattern;

/**
 * 正则表达式匹配类
 *
 * @author chenck
 * @date 2019/4/23 21:02
 */
public class GPTypePatternClassFilter implements GPClassFilter {

    private String typePattern = "";

    private Pattern pattern;

    public GPTypePatternClassFilter() {
    }

    public GPTypePatternClassFilter(String typePattern) {
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
    public boolean matches(Class<?> clazz) {
        return this.pattern.matcher(clazz.getName()).matches();
    }
}

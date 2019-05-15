package com.coy.gupaoedu.study.spring.framework.web.servlet;

import java.util.Locale;

/**
 * @author chenck
 * @date 2019/4/13 22:28
 */
public interface GPViewResolver {

    GPView resolveViewName(String viewName, Locale locale) throws Exception;
}

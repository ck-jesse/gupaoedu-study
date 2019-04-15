package com.coy.gupaoedu.study.spring.framework.web.servlet;

import com.sun.istack.internal.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author chenck
 * @date 2019/4/13 22:27
 */
public interface GPView {

    void render(@Nullable Map<String, ?> model,
                HttpServletRequest request, HttpServletResponse response) throws Exception;
}

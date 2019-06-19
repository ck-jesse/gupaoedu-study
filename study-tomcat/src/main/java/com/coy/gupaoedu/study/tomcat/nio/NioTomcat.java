package com.coy.gupaoedu.study.tomcat.nio;


import com.coy.gupaoedu.study.tomcat.WebXmlParseUtil;
import com.coy.gupaoedu.study.tomcat.nio.http.NioServlet;

import java.util.HashMap;
import java.util.Map;

/**
 * 简易版的tomcat
 * 基于Nio来实现
 * <p>
 * tomcat7中自动选取BIO来实现
 * tomcat8中自动选取NIO来实现
 *
 * @author chenck
 * @date 2019/6/18 20:28
 */
public class NioTomcat {
    private int port = 8080;

    private Map<String, NioServlet> servletMapping = new HashMap<String, NioServlet>();

    public void start() {
        //1、加载配置文件，初始化ServeltMapping
        servletMapping = WebXmlParseUtil.<NioServlet>parse("web-nio.properties");


    }


    /**
     * 简易版的tomcat
     * 使用：
     * 浏览器中输入地址： http://127.0.0.1:8080/firstServlet.do
     * 浏览器中输入地址： http://127.0.0.1:8080/secondServlet.do
     */
    public static void main(String[] args) {
        new NioTomcat().start();
    }
}

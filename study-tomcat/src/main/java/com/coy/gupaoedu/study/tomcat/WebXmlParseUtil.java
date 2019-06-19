package com.coy.gupaoedu.study.tomcat;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 加载web.xml文件,同时初始化 ServletMapping对象
 *
 * @author chenck
 * @date 2019/6/19 11:16
 */
public class WebXmlParseUtil {

    public static Properties webxml = new Properties();

    public static <T> Map<String, T> parse(String xmlname) {
        // servlet 容器
        Map<String, T> servletMapping = new HashMap<String, T>();
        // 加载web.xml文件,同时初始化 ServletMapping对象
        try {
            String WEB_INF = WebXmlParseUtil.class.getClass().getResource("/").getPath();
            FileInputStream fis = new FileInputStream(WEB_INF + xmlname);

            webxml.load(fis);

            // 解析
            for (Object k : webxml.keySet()) {
                String key = k.toString();
                if (key.endsWith(".url")) {
                    String servletName = key.replaceAll("\\.url$", "");
                    String url = webxml.getProperty(key);
                    String className = webxml.getProperty(servletName + ".className");
                    //单实例，多线程
                    T servlet = (T) Class.forName(className).newInstance();
                    System.out.println("Init servlet success " + className);
                    servletMapping.put(url, servlet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servletMapping;
    }
}

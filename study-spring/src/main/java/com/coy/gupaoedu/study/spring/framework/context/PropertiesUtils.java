package com.coy.gupaoedu.study.spring.framework.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 加载属性文件的工具类
 *
 * @author chenck
 * @date 2019/4/19 19:38
 */
public class PropertiesUtils {

    /**
     * 保存application.properties配置文件中的内容
     */
    private static Properties config = new Properties();

    public static Properties getConfig() {
        return config;
    }

    public static String getProperty(String key) {
        return config.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return config.getProperty(key, defaultValue);
    }

    /**
     * 加载属性文件内容
     */
    public static void load(String... contextConfigLocation) {
        InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(contextConfigLocation[0]);
        try {
            config.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 包扫描路径
     */
    public static String getScanPackage() {
        return config.getProperty("scanPackage");
    }

    /**
     * 启动切面aop代理 true表示启动 false表示不启动
     */
    public static boolean getAspectjAutoproxy() {
        String aspectjAutoproxy = config.getProperty("aspectj-autoproxy", "false");
        return "true".equalsIgnoreCase(aspectjAutoproxy);
    }

    // --------aspect配置------------------

    /**
     * 切面表达式
     */
    public static String getAspectPointcut() {
        return config.getProperty("aspectPointcut");
    }

    /**
     * 切面类
     */
    public static String getAspectClass() {
        return config.getProperty("aspectClass");
    }

    /**
     * 切面前置通知
     */
    public static String getAspectBefore() {
        return config.getProperty("aspectBefore");
    }

    /**
     * 切面后置通知
     */
    public static String getAspectAfter() {
        return config.getProperty("aspectAfter");
    }

    /**
     * 切面异常通知
     */
    public static String getAspectAfterThrow() {
        return config.getProperty("aspectAfterThrow");
    }

    /**
     * 切面异常类型
     */
    public static String getAspectAfterThrowingName() {
        return config.getProperty("aspectAfterThrowingName");
    }
}

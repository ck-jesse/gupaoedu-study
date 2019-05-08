package com.coy.gupaoedu.study.mebatis;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author chenck
 * @date 2019/5/7 19:53
 */
public class ClassUtil {

    /**
     * The package separator character: '.'
     */
    public static final String PACKAGE_SEPARATOR = ".";
    /**
     * The ".class" file suffix
     */
    public static final String CLASS_FILE_SUFFIX = ".class";

    /**
     * 当前的ClassLoader
     */
    public static ClassLoader currentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取classpath下指定path的所有资源
     */
    public static Enumeration<URL> getResources(String path) throws IOException {
        Enumeration<URL> resourceUrls = currentClassLoader().getResources(path);
        return resourceUrls;
    }

    /**
     * 扫描package下的类
     */
    public static List<Class<?>> scannerPackage(String scanPackage) throws Exception {
        return ClassUtil.scannerPackage(scanPackage, true);
    }

    /**
     * 扫描package下的类
     */
    public static List<Class<?>> scannerPackage(String scanPackage, boolean onlyInterface) throws Exception {
        List<Class<?>> mapperClass = new ArrayList<Class<?>>();
        // 获取package path
        String packageSearchPath = resolveBasePackage(scanPackage);
        Enumeration<URL> resourceUrls = getResources(packageSearchPath);
        while (resourceUrls.hasMoreElements()) {
            URL url = resourceUrls.nextElement();
            File classPath = new File(url.getFile());
            for (File file : classPath.listFiles()) {
                if (file.isDirectory()) {
                    mapperClass.addAll(scannerPackage(scanPackage + PACKAGE_SEPARATOR + file.getName(), onlyInterface));
                    continue;
                }
                if (!file.getName().endsWith(CLASS_FILE_SUFFIX)) {
                    continue;
                }
                String className = (scanPackage + PACKAGE_SEPARATOR + file.getName().replace(CLASS_FILE_SUFFIX, ""));
                Class type = Class.forName(className);
                if (type.isInterface() && onlyInterface) {
                    mapperClass.add(type);
                }
            }
        }
        return mapperClass;
    }

    /**
     * 将package转换为path
     */
    public static String resolveBasePackage(String basePackage) {
        return basePackage.replaceAll("\\.", "/");
    }
}

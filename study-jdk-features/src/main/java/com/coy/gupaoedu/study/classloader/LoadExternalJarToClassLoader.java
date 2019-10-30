package com.coy.gupaoedu.study.classloader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 加载外部jar到系统类加载器
 * <p>
 * 特别注意：如果是SpringBoot应用，按照mavne-spring-boot打包以后，类加载器变为了 org.springframework.boot.loader.JarLauncher
 * SpringBoot 自定义了类加载器 org.springframework.boot.loader.LaunchedURLClassLoader，
 * 加载 Spring-Boot-Classes: BOOT-INF/classes/ 和 Spring-Boot-Lib: BOOT-INF/lib/ 下面的class和第三方jar到类加载器，
 * 所以如果需要 动态加载外部的jar 到系统中，那么需要获取到类加载器 LaunchedURLClassLoader，然后再将外部jar加载
 *
 * @author chenck
 * @date 2019/10/30 9:52
 */
public class LoadExternalJarToClassLoader {

    /**
     * 获取系统类加载器
     * 实际获取到的是 sun.misc.Launcher.AppClassLoadersun.misc.Launcher.AppClassLoader
     */
    private static URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();

    /**
     * 定义 URLClassLoader 的 addURL 方法
     */
    private static Method addURLMethod = initAddURLMethod();

    /**
     * 获取 URLClassLoader 的 addURL 方法
     */
    private static Method initAddURLMethod() {
        try {
            Method addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            addURLMethod.setAccessible(true);
            return addURLMethod;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static URLClassLoader getUrlClassLoader() {
        return urlClassLoader;
    }

    /**
     * 加载外部jar
     */
    public static void loadExternalJar(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException(filePath + " 不存在");
        }

        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            for (File fileTemp : fileList) {
                loadExternalJar(fileTemp.getPath());
            }
        } else {
            if (file.getAbsolutePath().endsWith(".jar")) {
                try {
                    // 通过系统类加载器加载外部jar文件
                    addURLMethod.invoke(urlClassLoader, file.toURI().toURL());
                    System.out.println(urlClassLoader.getClass().getSimpleName() + " 加载外部jar成功: " + file.getPath());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        LoadExternalJarToClassLoader.loadExternalJar("D:\\.m2\\repository\\com\\hs\\protosdk\\platfrom-service\\dingtalk-service-api\\0.0.1");

        System.out.println();
        System.out.println(getUrlClassLoader().getClass().getSimpleName() + " 类加载器中加载的jar列表：");
        for (URL url : getUrlClassLoader().getURLs()) {
            System.out.println(url.toString());
        }
    }

}

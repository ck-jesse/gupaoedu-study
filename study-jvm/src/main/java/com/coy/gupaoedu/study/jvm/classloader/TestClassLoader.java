package com.coy.gupaoedu.study.jvm.classloader;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

/**
 * @author chenck
 * @date 2019/12/9 17:21
 */
public class TestClassLoader {

    public static void main(String[] args) throws Exception {
        String jarPath = "D:\\.m2\\repository\\commons-lang\\commons-lang\\2.6\\commons-lang-2.6.jar";
        String className = "org.apache.commons.lang.StringUtils";

        String jarPath1 = "D:\\.m2\\repository\\com\\hs\\protosdk\\platfrom-service\\id-generator-service-api\\0.0" +
                ".4-SNAPSHOT\\id-generator-service-api-0.0.4-SNAPSHOT.jar";
        String className1 = "com.hs.id.generator.proto.IdGeneratorProto";

        URLClassLoader classLoader = unloadJar(jarPath1, className1);
        System.out.println(classLoader.getURLs().length);
    }

    public static URLClassLoader unloadJar(String jarPath, String className) throws Exception {
        File jar = new File(jarPath);
        URL[] urls = new URL[]{jar.toURI().toURL()};
        URLClassLoader classLoader = new URLClassLoader(urls);
        Class<?> cls = classLoader.loadClass(className);
        System.out.println(cls.getName());


        // 查找URLClassLoader中的ucp
        Field ucpField = URLClassLoader.class.getDeclaredField("ucp");
        ucpField.setAccessible(true);
        Object ucpObj = ucpField.get(classLoader);

        URL[] urLs = classLoader.getURLs();
        for (int i = 0; i < urLs.length; i++) {
            // 获得ucp内部的jarLoader
            Method ucpMethod = ucpObj.getClass().getDeclaredMethod("getLoader", int.class);
            ucpMethod.setAccessible(true);
            Object jarLoader = ucpMethod.invoke(ucpObj, i);
            String clsName = jarLoader.getClass().getName();
            if (clsName.indexOf("JarLoader") != -1) {
                ucpMethod = jarLoader.getClass().getDeclaredMethod("ensureOpen");
                ucpMethod.setAccessible(true);
                ucpMethod.invoke(jarLoader);
                ucpMethod = jarLoader.getClass().getDeclaredMethod("getJarFile");
                ucpMethod.setAccessible(true);
                JarFile jf = (JarFile) ucpMethod.invoke(jarLoader);
                // 释放jarLoader中的jar文件
                jf.close();
                System.out.println("release jar: " + jf.getName());
            }
        }
        System.gc();
        cls = classLoader.loadClass(className);
        System.out.println(cls.getName());
        return classLoader;
    }

}

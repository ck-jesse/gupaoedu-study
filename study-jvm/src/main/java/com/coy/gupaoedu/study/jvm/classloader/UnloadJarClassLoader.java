package com.coy.gupaoedu.study.jvm.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * 自定义ClassLoader加载和卸载外部jar
 * <p>
 * 注：某些场景下，加载外部jar使用完毕后，需要卸载jar，然后再重新加载jar
 * <p>
 * 特别注意：如spring-boot自定义了类加载器，如果加载的外部jar需要使用到spring-boot的类加载器中的类，
 * 则必须将spring-boot的类加载器对象设置到该自定义ClassLoader中，作为该自定义ClassLoader的父类加载器，
 * 这样自定义ClassLoader中可以通过双亲委派机制来获取到父类加载器中的类对象。
 *
 * @author chenck
 * @date 2019/12/9 18:49
 */
public class UnloadJarClassLoader extends URLClassLoader {

    public UnloadJarClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public UnloadJarClassLoader(URL[] urls) {
        super(urls);
    }

    public UnloadJarClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }
}

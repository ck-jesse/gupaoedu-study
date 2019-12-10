package com.coy.gupaoedu.study.jvm.classloader;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenck
 * @date 2019/12/9 18:08
 */
public class DynamicJarClassLoader extends URLClassLoader {
//    private static boolean canCloseJar = false;
//    private List<JarURLConnection> cachedJarFiles;
    private Map<URL, JarURLConnection> cachedJarFilesMap;

    static {
        // 1.7之后可以直接调用close方法关闭打开的jar，需要判断当前运行的环境是否支持close方法，如果不支持，需要缓存，避免卸载模块后无法删除jar
        try {
            URLClassLoader.class.getMethod("close");
//            canCloseJar = true;
        } catch (NoSuchMethodException e) {
        } catch (SecurityException e) {
        }
    }

    public DynamicJarClassLoader(URL[] urls, ClassLoader parent) {
        super(new URL[]{}, parent);
        init(urls);
    }

    public DynamicJarClassLoader(URL[] urls) {
        super(new URL[]{});
        init(urls);
    }

    public DynamicJarClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(new URL[]{}, parent, factory);
        init(urls);
    }

    private void init(URL[] urls) {
        cachedJarFilesMap = new HashMap<>();
        if (urls != null) {
            for (URL url : urls) {
                this.addURL(url);
            }
        }
    }

    @Override
    protected void addURL(URL url) {
        try {
            // 打开并缓存文件url连接
            URLConnection uc = url.openConnection();
            if (uc instanceof JarURLConnection) {
                uc.setUseCaches(true);
                ((JarURLConnection) uc).getManifest();
                //cachedJarFiles.add((JarURLConnection) uc);
                cachedJarFilesMap.put(url, (JarURLConnection) uc);
            }
        } catch (Exception e) {
        }
        super.addURL(url);
    }

    public void unloadJarFile(String url) {
        URL jarURL = getURL(url);
        unloadJarFile(jarURL);
    }

    public void unloadJarFile(URL jarURL) {
        JarURLConnection jarURLConnection = cachedJarFilesMap.get(jarURL);
        if (jarURLConnection == null) {
            return;
        }
        try {
            System.err.println("Unloading plugin JAR file " + jarURLConnection.getJarFile().getName());
            jarURLConnection.getJarFile().close();
            jarURLConnection = null;
            cachedJarFilesMap.remove(jarURL);
            // System.gc();
        } catch (Exception e) {
            System.err.println("Failed to unload JAR file\n" + e);
        }
    }

    public static URL getURL(String url) {
        try {
            return new File(url).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void close() throws IOException {
        for (Map.Entry<URL, JarURLConnection> urlJarURLConnectionEntry : cachedJarFilesMap.entrySet()) {
            urlJarURLConnectionEntry.getValue().getJarFile().close();
        }
        cachedJarFilesMap.clear();
    }

    public static void main(String[] args) {
        String jarPath = "D:\\.m2\\repository\\commons-lang\\commons-lang\\2.6\\commons-lang-2.6.jar";
        URL[] urls = new URL[]{getURL(jarPath)};

        DynamicJarClassLoader dynamicJarClassLoader = new DynamicJarClassLoader(urls);
        dynamicJarClassLoader.unloadJarFile(urls[0]);
        System.out.println();
    }
}

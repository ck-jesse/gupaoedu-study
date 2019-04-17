package com.coy.gupaoedu.study.spring.framework.beans;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author chenck
 * @date 2019/4/10 21:59
 */
public class GPBeanDefinitionReader {

    //保存application.properties配置文件中的内容
    private Properties config = new Properties();

    //保存扫描的所有的类名
    private List<String> classNames = new ArrayList<String>();

    public GPBeanDefinitionReader(String... contextConfigLocation) {

        doLoadConfig(contextConfigLocation);

        doScanner(config.getProperty("scanPackage"));

    }

    private void doLoadConfig(String... contextConfigLocation) {
        //直接从类路径下找到Spring主配置文件所在的路径
        //并且将其读取出来放到Properties对象中
        //相对于scanPackage=com.gupaoedu.demo 从文件中保存到了内存中
        InputStream fis = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation[0]);
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

    //扫描出相关的类
    private void doScanner(String scanPackage) {
        //scanPackage = com.gupaoedu.demo ，存储的是包路径
        //转换为文件路径，实际上就是把.替换为/就OK了
        //classpath
        URL url = this.getClass().getClassLoader().getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String className = (scanPackage + "." + file.getName().replace(".class", ""));
                classNames.add(className);
            }
        }
    }

    public Properties getConfig() {
        return this.config;
    }

    public List<GPBeanDefinition> loadBeanDefinitions() {
        List<GPBeanDefinition> beanDefinitions = new ArrayList<GPBeanDefinition>();
        for (String className : classNames) {
            GPBeanDefinition beanDefinition = doCreateBeanDefinition(className);
            if (null == beanDefinition) {
                continue;
            }
            beanDefinitions.add(beanDefinition);
        }
        return beanDefinitions;
    }

    /**
     * TODO
     */
    private GPBeanDefinition doCreateBeanDefinition(String className) {
        try {
            GPBeanDefinition beanDefinition = new GPBeanDefinition();
            Class clazz = Class.forName(className);
            // 注意对接口进行处理
            if (!clazz.isInterface()) {

            }
            beanDefinition.setBeanClass(clazz);
            beanDefinition.setBeanClassName(className);
            beanDefinition.setFactoryBeanName(clazz.getName());
            return beanDefinition;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

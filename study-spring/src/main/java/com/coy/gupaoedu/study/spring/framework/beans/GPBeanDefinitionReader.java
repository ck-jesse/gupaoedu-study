package com.coy.gupaoedu.study.spring.framework.beans;

import com.coy.gupaoedu.study.spring.framework.aop.framework.autoproxy.GPAbstractAutoProxyCreator;
import com.coy.gupaoedu.study.spring.framework.context.PropertiesUtils;
import com.coy.gupaoedu.study.spring.framework.context.annotation.GPComponent;
import com.coy.gupaoedu.study.spring.framework.context.annotation.GPController;
import com.coy.gupaoedu.study.spring.framework.context.annotation.GPService;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Bean定义的读取器
 *
 * @author chenck
 * @date 2019/4/10 21:59
 */
public class GPBeanDefinitionReader {

    /**
     * prefix for all matching resources from the class path
     */
    public static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    public static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    /**
     * The package separator character: '.'
     */
    public static final String PACKAGE_SEPARATOR = ".";
    /**
     * The path separator character: '/'
     */
    public static final String PATH_SEPARATOR = "/";
    /**
     * The ".class" file suffix
     */
    public static final String CLASS_FILE_SUFFIX = ".class";

    /**
     * The bean name of the internally managed auto-proxy creator.
     */
    public static final String AUTO_PROXY_CREATOR_BEAN_NAME = "org.springframework.aop.config.internalAutoProxyCreator";

    /**
     * 保存扫描的所有的类名
     */
    private List<String> classNames = new ArrayList<String>();

    public GPBeanDefinitionReader() {
        doScanner(PropertiesUtils.getScanPackage());
    }

    /**
     * 扫描出相关的类
     */
    private void doScanner(String scanPackage) {
        try {
            // 获取package path
            String packageSearchPath = resolveBasePackage(scanPackage);
            Enumeration<URL> resourceUrls = Thread.currentThread().getContextClassLoader().getResources(packageSearchPath);
            while (resourceUrls.hasMoreElements()) {
                URL url = resourceUrls.nextElement();
                File classPath = new File(url.getFile());
                for (File file : classPath.listFiles()) {
                    if (file.isDirectory()) {
                        doScanner(scanPackage + PACKAGE_SEPARATOR + file.getName());
                        continue;
                    }
                    if (!file.getName().endsWith(CLASS_FILE_SUFFIX)) {
                        continue;
                    }
                    String className = (scanPackage + PACKAGE_SEPARATOR + file.getName().replace(CLASS_FILE_SUFFIX, ""));
                    classNames.add(className);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 将package转换为path
     */
    protected String resolveBasePackage(String basePackage) {
        return basePackage.replaceAll("\\.", "/");
    }

    /**
     * 加载bean定义
     */
    public List<GPBeanDefinition> loadBeanDefinitions() {
        List<GPBeanDefinition> beanDefinitions = new ArrayList<GPBeanDefinition>();

        // 普通bean定义的处理
        for (String className : classNames) {
            GPBeanDefinition beanDefinition = doCreateBeanDefinition(className);
            if (null == beanDefinition) {
                continue;
            }
            beanDefinitions.add(beanDefinition);
        }
        // aspectj-autoproxy AOP代理bean定义的处理
        if (PropertiesUtils.getAspectjAutoproxy()) {
            GPBeanDefinition beanDefinition = new GPBeanDefinition();
            beanDefinition.setFactoryBeanName(AUTO_PROXY_CREATOR_BEAN_NAME);
            beanDefinition.setBeanClass(GPAbstractAutoProxyCreator.class);
            beanDefinitions.add(beanDefinition);
        }
        return beanDefinitions;
    }

    /**
     * 创建BeanDefinition
     */
    private GPBeanDefinition doCreateBeanDefinition(String className) {
        try {
            Class clazz = Class.forName(className);
            // 排除接口和抽象类
            // 注：spring是通过ClassMetadata来存放class信息的，其中通过org.springframework.core.type.StandardClassMetadata.isConcrete()排除掉接口和抽象类
            if (Modifier.isInterface(clazz.getModifiers())) {
                System.out.println("忽略 interface " + className);
                return null;
            }
            if (Modifier.isAbstract(clazz.getModifiers())) {
                System.out.println("忽略 abstract class " + className);
                return null;
            }

            // 判断class是否为候选的bean，过滤掉非
            if (!isCandidateComponent(clazz)) {
                System.out.println("忽略class，未定义@Component等注解，" + className);
                return null;
            }

            GPBeanDefinition beanDefinition = new GPBeanDefinition();
            boolean isAbstract = Modifier.isAbstract(clazz.getModifiers());
            beanDefinition.setAbstractFlag(isAbstract);
            beanDefinition.setBeanClass(clazz);
            beanDefinition.setBeanClassName(className);
            beanDefinition.setFactoryBeanName(clazz.getName());
            return beanDefinition;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断class是否为候选的bean
     */
    private boolean isCandidateComponent(Class clazz) {
        if (clazz.isAnnotationPresent(GPComponent.class)) {
            return true;
        }
        if (clazz.isAnnotationPresent(GPController.class)) {
            return true;
        }
        if (clazz.isAnnotationPresent(GPService.class)) {
            return true;
        }
        return false;
    }
}

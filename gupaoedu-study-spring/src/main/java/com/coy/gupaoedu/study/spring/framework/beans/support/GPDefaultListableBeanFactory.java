package com.coy.gupaoedu.study.spring.framework.beans.support;

import com.coy.gupaoedu.study.spring.framework.beans.GPBeanDefinition;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenck
 * @date 2019/4/10 21:43
 */
public class GPDefaultListableBeanFactory implements GPBeanFactory {

    // Map<BeanName, GPBeanDefinition>
    private final Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap(256);

    @Override
    public Object getBean(String beanName) {

        // 初始化

        // 注入
        return null;
    }

    @Override
    public <T> T getBean(Class<T> beanClazz) {
        return null;
    }

    @Override
    public GPBeanDefinition getBeanDefinition(String beanName) {
        return null;
    }
}

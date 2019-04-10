package com.coy.gupaoedu.study.spring.framework.beans;

import lombok.Data;

/**
 * @author chenck
 * @date 2019/4/10 21:57
 */
@Data
public class GPBeanDefinition {

    private String beanClassName;
    private String factoryBeanName;
    private boolean lazyInit;
}

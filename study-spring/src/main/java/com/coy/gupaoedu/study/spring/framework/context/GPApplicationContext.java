package com.coy.gupaoedu.study.spring.framework.context;

import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;

/**
 * @author chenck
 * @date 2019/4/10 21:31
 */
public interface GPApplicationContext extends GPBeanFactory {

    GPBeanFactory getBeanFactory();

    void refresh();
}

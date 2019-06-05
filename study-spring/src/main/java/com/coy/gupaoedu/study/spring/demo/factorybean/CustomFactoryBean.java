package com.coy.gupaoedu.study.spring.demo.factorybean;

import com.coy.gupaoedu.study.spring.framework.beans.GPFactoryBean;
import com.coy.gupaoedu.study.spring.framework.context.annotation.GPComponent;

/**
 * 自定义FactoryBean，目的为自定义Bean的创建过程
 *
 * @author chenck
 * @date 2019/6/3 20:44
 */
@GPComponent
public class CustomFactoryBean implements GPFactoryBean<FactoryBeanService> {

    /**
     * If this is a singleton, the cached singleton proxy instance
     */
    private FactoryBeanService singletonInstance;

    @Override
    public FactoryBeanService getObject() throws Exception {
        // 这个Bean是我们自己new的，这里我们就可以控制Bean的创建过程了
        System.out.println("通过FactoryBean控制bean的创建过程");
        if (isSingleton()) {
            return getSingletonInstance();
        }
        return new FactoryBeanServiceImpl();
    }

    @Override
    public Class<?> getObjectType() {
        return FactoryBeanService.class;
    }

    private FactoryBeanService getSingletonInstance() {
        if (null == singletonInstance) {
            singletonInstance = new FactoryBeanServiceImpl();
        }
        return singletonInstance;
    }
}

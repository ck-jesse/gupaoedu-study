package com.coy.gupaoedu.study.spring.framework.beans;

/**
 * @author chenck
 * @date 2019/4/22 15:05
 */
public interface GPBeanNameAware extends GPAware {
    /**
     * 包装bean名称
     * Set the name of the bean in the bean factory that created this bean.
     * <p>Invoked after population of normal bean properties but before an
     * init callback such as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method.
     *
     * @param name the name of the bean in the factory.
     *             Note that this name is the actual bean name used in the factory, which may
     *             differ from the originally specified name: in particular for inner bean
     *             names, the actual bean name might have been made unique through appending
     *             "#..." suffixes. Use the {@link BeanFactoryUtils#originalBeanName(String)}
     *             method to extract the original bean name (without suffix), if desired.
     */
    void setBeanName(String name);
}

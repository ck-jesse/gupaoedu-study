package com.coy.gupaoedu.study.spring.framework.aop.framework.autoproxy;

import com.coy.gupaoedu.study.spring.framework.aop.GPAdvisor;
import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.beans.exception.GPBeansException;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;
import com.sun.istack.internal.Nullable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * 初始化Advisor的检索工具类
 *
 * @author chenck
 * @date 2019/4/22 20:41
 */
public class GPBeanFactoryAdvisorRetrievalHelper {

    private static final Log logger = LogFactory.getLog(GPBeanFactoryAdvisorRetrievalHelper.class);

    private final GPBeanFactory beanFactory;

    @Nullable
    private String[] cachedAdvisorBeanNames;


    /**
     * Create a new BeanFactoryAdvisorRetrievalHelper for the given BeanFactory.
     *
     * @param beanFactory the ListableBeanFactory to scan
     */
    public GPBeanFactoryAdvisorRetrievalHelper(GPBeanFactory beanFactory) {
        Assert.notNull(beanFactory, "ListableBeanFactory must not be null");
        this.beanFactory = beanFactory;
    }


    /**
     * 在当前bean工厂中查找所有符合条件的Advisor bean
     * <p>
     * Find all eligible Advisor beans in the current bean factory,
     * ignoring FactoryBeans and excluding beans that are currently in creation.
     *
     * @see #isEligibleBean
     */
    public List<GPAdvisor> findAdvisorBeans() {
        // Determine list of advisor bean names, if not cached already.
        String[] advisorNames = null;
        synchronized (this) {
            advisorNames = this.cachedAdvisorBeanNames;
            if (advisorNames == null) {
                // Do not initialize FactoryBeans here: We need to leave all regular beans
                // uninitialized to let the auto-proxy creator apply to them!
                //advisorNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
                //        this.beanFactory, GPAdvisor.class, true, false);
                advisorNames = this.beanFactory.getBeanNamesForType(GPAdvisor.class, true);
                this.cachedAdvisorBeanNames = advisorNames;
            }
        }
        if (advisorNames.length == 0) {
            return new LinkedList<>();
        }

        List<GPAdvisor> advisors = new LinkedList<>();
        for (String name : advisorNames) {
            if (isEligibleBean(name)) {
                try {
                    advisors.add(this.beanFactory.getBean(name, GPAdvisor.class));
                } catch (GPBeansException ex) {
                    throw ex;
                }
            }
        }
        return advisors;
    }

    /**
     * 是否为合格的bean
     * Determine whether the aspect bean with the given name is eligible.
     * <p>The default implementation always returns {@code true}.
     *
     * @param beanName the name of the aspect bean
     * @return whether the bean is eligible
     */
    protected boolean isEligibleBean(String beanName) {
        return true;
    }

}

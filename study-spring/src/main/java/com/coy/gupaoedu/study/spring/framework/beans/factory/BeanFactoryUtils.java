/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coy.gupaoedu.study.spring.framework.beans.factory;

import com.coy.gupaoedu.study.spring.framework.beans.GPBeanFactory;
import com.coy.gupaoedu.study.spring.framework.core.util.Assert;

public abstract class BeanFactoryUtils {

    /**
     * Separator for generated bean names. If a class name or parent name is not
     * unique, "#1", "#2" etc will be appended, until the name becomes unique.
     */
    public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";


    /**
     * name是否包含FactoryBean的前缀，若包含，则该name对应的bean为FactoryBean的子类
     * Return whether the given name is a factory dereference
     * (beginning with the factory dereference prefix).
     *
     * @param name the name of the bean
     * @return whether the given name is a factory dereference
     */
    public static boolean isFactoryDereference(String name) {
        return (name != null && name.startsWith(GPBeanFactory.FACTORY_BEAN_PREFIX));
    }

    /**
     * 如果name包含FactoryBean的前缀，则进行处理并返回真实的beanName
     * <p>
     * Return the actual bean name, stripping out the factory dereference
     * prefix (if any, also stripping repeated factory prefixes if found).
     *
     * @param name the name of the bean
     * @return the transformed name
     */
    public static String transformedBeanName(String name) {
        Assert.notNull(name, "'name' must not be null");
        String beanName = name;
        while (beanName.startsWith(GPBeanFactory.FACTORY_BEAN_PREFIX)) {
            beanName = beanName.substring(GPBeanFactory.FACTORY_BEAN_PREFIX.length());
        }
        return beanName;
    }

    /**
     * Return whether the given name is a bean name which has been generated
     * by the default naming strategy (containing a "#..." part).
     *
     * @param name the name of the bean
     * @return whether the given name is a generated bean name
     * @see #GENERATED_BEAN_NAME_SEPARATOR
     */
    public static boolean isGeneratedBeanName(String name) {
        return (name != null && name.contains(GENERATED_BEAN_NAME_SEPARATOR));
    }

    /**
     * Extract the "raw" bean name from the given (potentially generated) bean name,
     * excluding any "#..." suffixes which might have been added for uniqueness.
     *
     * @param name the potentially generated bean name
     * @return the raw bean name
     * @see #GENERATED_BEAN_NAME_SEPARATOR
     */
    public static String originalBeanName(String name) {
        Assert.notNull(name, "'name' must not be null");
        int separatorIndex = name.indexOf(GENERATED_BEAN_NAME_SEPARATOR);
        return (separatorIndex != -1 ? name.substring(0, separatorIndex) : name);
    }


}

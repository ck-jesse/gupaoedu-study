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

package com.coy.gupaoedu.study.spring.framework.beans.support;

import com.coy.gupaoedu.study.spring.framework.beans.GPFactoryBean;

/**
 * Internal representation of a null bean instance, e.g. for a {@code null} value
 * returned from {@link GPFactoryBean#getObject()} or from a factory method.
 *
 * <p>Each such null bean is represented by a dedicated {@code NullBean} instance
 * which are not equal to each other, uniquely differentiating each bean as returned
 * However, each such instance will return {@code true} for {@code #equals(null)}
 * and returns "null" from {@code #toString()}, which is how they can be tested
 * externally (since this class itself is not public).
 *
 * @author Juergen Hoeller
 * @since 5.0
 */
final class NullBean {

    NullBean() {
    }


    @Override
    public boolean equals(Object obj) {
        return (this == obj || obj == null);
    }

    @Override
    public int hashCode() {
        return NullBean.class.hashCode();
    }

    @Override
    public String toString() {
        return "null";
    }

}

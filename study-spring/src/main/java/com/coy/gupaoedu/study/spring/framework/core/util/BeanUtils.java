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

package com.coy.gupaoedu.study.spring.framework.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Static convenience methods for JavaBeans: for instantiating beans,
 * checking bean property types, copying bean properties, etc.
 *
 * <p>Mainly for use within the framework, but to some degree also
 * useful for application classes.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Sam Brannen
 * @author Sebastien Deleuze
 */
public abstract class BeanUtils {

    private static final Log logger = LogFactory.getLog(BeanUtils.class);


    /**
     * Find a method with the given method name and the given parameter types,
     * declared on the given class or one of its superclasses. Prefers public methods,
     * but will return a protected, package access, or private method too.
     * <p>Checks {@code Class.getMethod} first, falling back to
     * {@code findDeclaredMethod}. This allows to find public methods
     * without issues even in environments with restricted Java security settings.
     *
     * @param clazz      the class to check
     * @param methodName the name of the method to find
     * @param paramTypes the parameter types of the method to find
     * @return the Method object, or {@code null} if not found
     * @see Class#getMethod
     * @see #findDeclaredMethod
     */
    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            return clazz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ex) {
            return findDeclaredMethod(clazz, methodName, paramTypes);
        }
    }

    /**
     * Find a method with the given method name and the given parameter types,
     * declared on the given class or one of its superclasses. Will return a public,
     * protected, package access, or private method.
     * <p>Checks {@code Class.getDeclaredMethod}, cascading upwards to all superclasses.
     *
     * @param clazz      the class to check
     * @param methodName the name of the method to find
     * @param paramTypes the parameter types of the method to find
     * @return the Method object, or {@code null} if not found
     * @see Class#getDeclaredMethod
     */

    public static Method findDeclaredMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, paramTypes);
        } catch (NoSuchMethodException ex) {
            if (clazz.getSuperclass() != null) {
                return findDeclaredMethod(clazz.getSuperclass(), methodName, paramTypes);
            }
            return null;
        }
    }

    /**
     * Find a method with the given method name and minimal parameters (best case: none),
     * declared on the given class or one of its superclasses. Prefers public methods,
     * but will return a protected, package access, or private method too.
     * <p>Checks {@code Class.getMethods} first, falling back to
     * {@code findDeclaredMethodWithMinimalParameters}. This allows for finding public
     * methods without issues even in environments with restricted Java security settings.
     *
     * @param clazz      the class to check
     * @param methodName the name of the method to find
     * @return the Method object, or {@code null} if not found
     * @throws IllegalArgumentException if methods of the given name were found but
     *                                  could not be resolved to a unique method with minimal parameters
     * @see Class#getMethods
     * @see #findDeclaredMethodWithMinimalParameters
     */

    public static Method findMethodWithMinimalParameters(Class<?> clazz, String methodName)
            throws IllegalArgumentException {

        Method targetMethod = findMethodWithMinimalParameters(clazz.getMethods(), methodName);
        if (targetMethod == null) {
            targetMethod = findDeclaredMethodWithMinimalParameters(clazz, methodName);
        }
        return targetMethod;
    }

    /**
     * Find a method with the given method name and minimal parameters (best case: none),
     * declared on the given class or one of its superclasses. Will return a public,
     * protected, package access, or private method.
     * <p>Checks {@code Class.getDeclaredMethods}, cascading upwards to all superclasses.
     *
     * @param clazz      the class to check
     * @param methodName the name of the method to find
     * @return the Method object, or {@code null} if not found
     * @throws IllegalArgumentException if methods of the given name were found but
     *                                  could not be resolved to a unique method with minimal parameters
     * @see Class#getDeclaredMethods
     */

    public static Method findDeclaredMethodWithMinimalParameters(Class<?> clazz, String methodName)
            throws IllegalArgumentException {

        Method targetMethod = findMethodWithMinimalParameters(clazz.getDeclaredMethods(), methodName);
        if (targetMethod == null && clazz.getSuperclass() != null) {
            targetMethod = findDeclaredMethodWithMinimalParameters(clazz.getSuperclass(), methodName);
        }
        return targetMethod;
    }

    /**
     * Find a method with the given method name and minimal parameters (best case: none)
     * in the given list of methods.
     *
     * @param methods    the methods to check
     * @param methodName the name of the method to find
     * @return the Method object, or {@code null} if not found
     * @throws IllegalArgumentException if methods of the given name were found but
     *                                  could not be resolved to a unique method with minimal parameters
     */
    public static Method findMethodWithMinimalParameters(Method[] methods, String methodName)
            throws IllegalArgumentException {

        Method targetMethod = null;
        int numMethodsFoundWithCurrentMinimumArgs = 0;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                int numParams = method.getParameterCount();
                if (targetMethod == null || numParams < targetMethod.getParameterCount()) {
                    targetMethod = method;
                    numMethodsFoundWithCurrentMinimumArgs = 1;
                } else if (!method.isBridge() && targetMethod.getParameterCount() == numParams) {
                    if (targetMethod.isBridge()) {
                        // Prefer regular method over bridge...
                        targetMethod = method;
                    } else {
                        // Additional candidate with same length
                        numMethodsFoundWithCurrentMinimumArgs++;
                    }
                }
            }
        }
        if (numMethodsFoundWithCurrentMinimumArgs > 1) {
            throw new IllegalArgumentException("Cannot resolve method '" + methodName +
                    "' to a unique method. Attempted to resolve to overloaded method with " +
                    "the least number of parameters but there were " +
                    numMethodsFoundWithCurrentMinimumArgs + " candidates.");
        }
        return targetMethod;
    }

    /**
     * Parse a method signature in the form {@code methodName[([arg_list])]},
     * where {@code arg_list} is an optional, comma-separated list of fully-qualified
     * type names, and attempts to resolve that signature against the supplied {@code Class}.
     * <p>When not supplying an argument list ({@code methodName}) the method whose name
     * matches and has the least number of parameters will be returned. When supplying an
     * argument type list, only the method whose name and argument types match will be returned.
     * <p>Note then that {@code methodName} and {@code methodName()} are <strong>not</strong>
     * resolved in the same way. The signature {@code methodName} means the method called
     * {@code methodName} with the least number of arguments, whereas {@code methodName()}
     * means the method called {@code methodName} with exactly 0 arguments.
     * <p>If no method can be found, then {@code null} is returned.
     *
     * @param signature the method signature as String representation
     * @param clazz     the class to resolve the method signature against
     * @return the resolved Method
     * @see #findMethod
     * @see #findMethodWithMinimalParameters
     */
    public static Method resolveSignature(String signature, Class<?> clazz) {
        Assert.hasText(signature, "'signature' must not be empty");
        Assert.notNull(clazz, "Class must not be null");
        int firstParen = signature.indexOf("(");
        int lastParen = signature.indexOf(")");
        if (firstParen > -1 && lastParen == -1) {
            throw new IllegalArgumentException("Invalid method signature '" + signature +
                    "': expected closing ')' for args list");
        } else if (lastParen > -1 && firstParen == -1) {
            throw new IllegalArgumentException("Invalid method signature '" + signature +
                    "': expected opening '(' for args list");
        } else if (firstParen == -1 && lastParen == -1) {
            return findMethodWithMinimalParameters(clazz, signature);
        } else {
            String methodName = signature.substring(0, firstParen);
            String[] parameterTypeNames =
                    StringUtils.commaDelimitedListToStringArray(signature.substring(firstParen + 1, lastParen));
            Class<?>[] parameterTypes = new Class<?>[parameterTypeNames.length];
            for (int i = 0; i < parameterTypeNames.length; i++) {
                String parameterTypeName = parameterTypeNames[i].trim();
                try {
                    parameterTypes[i] = ClassUtils.forName(parameterTypeName, clazz.getClassLoader());
                } catch (Throwable ex) {
                    throw new IllegalArgumentException("Invalid method signature: unable to resolve type [" +
                            parameterTypeName + "] for argument " + i + ". Root cause: " + ex);
                }
            }
            return findMethod(clazz, methodName, parameterTypes);
        }
    }


    /**
     * Check if the given type represents a "simple" property:
     * a primitive, a String or other CharSequence, a Number, a Date,
     * a URI, a URL, a Locale, a Class, or a corresponding array.
     * <p>Used to determine properties to check for a "simple" dependency-check.
     *
     * @param clazz the type to check
     * @return whether the given type represents a "simple" property
     * @see org.springframework.beans.factory.support.RootBeanDefinition#DEPENDENCY_CHECK_SIMPLE
     * @see org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#checkDependencies
     */
    public static boolean isSimpleProperty(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
    }

    /**
     * Check if the given type represents a "simple" value type:
     * a primitive, a String or other CharSequence, a Number, a Date,
     * a URI, a URL, a Locale or a Class.
     *
     * @param clazz the type to check
     * @return whether the given type represents a "simple" value type
     */
    public static boolean isSimpleValueType(Class<?> clazz) {
        return (ClassUtils.isPrimitiveOrWrapper(clazz) || clazz.isEnum() ||
                CharSequence.class.isAssignableFrom(clazz) ||
                Number.class.isAssignableFrom(clazz) ||
                Date.class.isAssignableFrom(clazz) ||
                URI.class == clazz || URL.class == clazz ||
                Locale.class == clazz || Class.class == clazz);
    }


}

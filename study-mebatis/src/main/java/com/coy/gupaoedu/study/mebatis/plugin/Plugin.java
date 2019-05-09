package com.coy.gupaoedu.study.mebatis.plugin;

import com.coy.gupaoedu.study.mebatis.exception.PluginException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 代理类，用于代理被拦截对象
 * 同时提供了创建代理类的方法
 */
public class Plugin implements InvocationHandler {
    private Object target;
    private Interceptor interceptor;
    private final Map<Class<?>, Set<Method>> signatureMap;

    /**
     * 实例化
     *
     * @param target       被代理对象
     * @param interceptor  拦截器（插件）
     * @param signatureMap 拦截目标的方法签名信息
     */
    public Plugin(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap) {
        this.target = target;
        this.interceptor = interceptor;
        this.signatureMap = signatureMap;
    }

    /**
     * 对被代理对象进行代理，返回代理类
     *
     * @param target
     * @param interceptor
     * @return
     */
    public static Object wrap(Object target, Interceptor interceptor) {
        Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
        Class<?> type = target.getClass();
        Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
        if (null == interfaces || interfaces.length == 0) {
            return target;
        }
        return Proxy.newProxyInstance(type.getClassLoader(), interfaces, new Plugin(target, interceptor, signatureMap));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 检查method是否是被拦截的方法，则进入自定义拦截器的逻辑
        Set<Method> methods = signatureMap.get(method.getDeclaringClass());
        if (methods != null && methods.contains(method)) {
            return interceptor.intercept(new Invocation(target, method, args));
        }
        // 非被拦截方法，执行原逻辑
        return method.invoke(target, args);
    }

    /**
     * 拦截器上定义的签名解析
     */
    private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) {
        // 判断拦截器是否有定义@Intercepts
        Intercepts interceptsAnnotation = interceptor.getClass().getAnnotation(Intercepts.class);
        if (interceptsAnnotation == null) {
            throw new PluginException("No @Intercepts annotation was found in interceptor " + interceptor.getClass().getName());
        }
        // 对签名进行解析
        Signature[] signs = interceptsAnnotation.value();
        Map<Class<?>, Set<Method>> signatureMap = new HashMap<Class<?>, Set<Method>>();
        for (Signature sign : signs) {
            Set<Method> methods = signatureMap.get(sign.type());
            if (methods == null) {
                methods = new HashSet<Method>();
                signatureMap.put(sign.type(), methods);
            }
            try {
                // method必须与type中的方法匹配
                Method method = sign.type().getMethod(sign.method(), sign.args());
                methods.add(method);
            } catch (NoSuchMethodException e) {
                throw new PluginException("Could not find method on " + sign.type() + " named " + sign.method() + ". Cause: " + e, e);
            }
        }
        return signatureMap;
    }

    /**
     * 获取所有的接口（含父类实现的接口）
     * 注：过滤掉Intercepter上未配置的interface
     */
    private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        while (type != null) {
            for (Class<?> c : type.getInterfaces()) {
                // 过滤掉未配置的interface
                if (signatureMap.containsKey(c)) {
                    interfaces.add(c);
                }
            }
            // 循环获取type父类实现的接口
            type = type.getSuperclass();
        }
        return interfaces.toArray(new Class<?>[interfaces.size()]);
    }
}

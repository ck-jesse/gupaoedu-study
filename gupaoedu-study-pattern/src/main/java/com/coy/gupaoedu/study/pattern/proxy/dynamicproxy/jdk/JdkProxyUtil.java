package com.coy.gupaoedu.study.pattern.proxy.dynamicproxy.jdk;

import java.lang.reflect.Proxy;

/**
 * JDK代理工具类<br>
 * 1.JDK动态代理：通过反射机制针对实现了接口的类进行代理。 <br>
 * 动态配置和替换被代理对象
 *
 * @author chenck
 * @date 2017年3月15日 下午2:36:03
 */
public class JdkProxyUtil {

    /**
     * 【JDK动态代理】创建代理实例 <br>
     *
     * @param targetObject 目标对象
     * @param handler      代理处理器
     * @return
     * @author chenck
     * @date 2017年3月15日 下午2:59:12
     */
    public static Object createProxy(Object targetObject, AbstractProxyHandler handler) {
        if (null == handler) {
            throw new IllegalArgumentException("handler is null");
        }
        // 设置代理目标对象
        handler.setTargetObject(targetObject);

        // 通过反射机制为目标对象动态创建代理实例
        // JDK动态代理的实现原理：
        // 1、拿到被代理类的引用，并且获取它的所有的接口（反射获取）。
        // 2、JDKProxy类重新生成一个新的类，实现了被代理类所有接口的方法（JDK动态代理是实现了被代理对象的接口）
        // 3、动态生成Java代码，把增强逻辑加入到新生成的类代码中
        // 4、编译生成新的Java代码的class文件。
        // 5、加载并重新运行新的class，得到类就是全新类。
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),
                targetObject.getClass().getInterfaces(), handler);
    }

}

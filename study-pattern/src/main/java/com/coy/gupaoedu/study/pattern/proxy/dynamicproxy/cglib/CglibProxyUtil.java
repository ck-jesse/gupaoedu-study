package com.coy.gupaoedu.study.pattern.proxy.dynamicproxy.cglib;

import net.sf.cglib.proxy.Enhancer;

/**
 * CGLIB 代理工具类<br>
 * 2.CGLIB动态代理：通过字节码技术针对类进行代理。<br>
 * 注：CGLIB动态代理原理：对指定的目标类生成一个子类，并覆盖其中方法实现增强，但因为采用的是继承，所以不能对final修饰的类进行代理。<br>
 * <p>
 * 无法代理final修饰的方法。
 *
 * @author chenck
 * @date 2019/3/20 21:03
 */
public class CglibProxyUtil {

    /**
     * 【CGLIB动态代理】创建代理实例 <br>
     *
     * @param targetObject 目标对象
     * @param handler      代理处理器
     * @return
     * @author chenck
     * @date 2017年3月15日 下午2:59:12
     */
    public static Object createProxyCglib(Object targetObject, AbstractProxyIntercepter handler) {
        if (null == handler) {
            throw new IllegalArgumentException("handler is null");
        }
        // 设置代理目标对象
        handler.setTargetObject(targetObject);

        Enhancer enhancer = new Enhancer();
        // 设置需要创建子类的目标对象class
        enhancer.setSuperclass(targetObject.getClass());
        // 回调方法
        enhancer.setCallback(handler);
        // 通过字节码技术为目标对象动态创建子类实例
        return enhancer.create();
    }
}

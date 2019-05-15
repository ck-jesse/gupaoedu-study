package com.coy.gupaoedu.study.pattern.singleton.container;

import com.coy.gupaoedu.study.pattern.observer.observer.Daughter;
import com.coy.gupaoedu.study.pattern.observer.observer.Mother;
import com.coy.gupaoedu.study.pattern.observer.observer.Son;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于容器来实现单例模式
 * 工厂模式+单例模式
 * 注：设计模式往往是混合在一起使用的，要特别注意！！！
 * <p>
 * 有很多框架是基于该方式来实现的，比如spring，所以有很高的实用价值
 *
 * @author chenck
 * @date 2019/3/20 14:00
 */
public class ContainerSingleton {

    /**
     * 模拟bean的来源
     * 注：往往在实际的业务场景中，会需要限制只能生成某一类的bean的实例，也就是限制了bean范围
     * 当然也可以结合spring容器来实现
     */
    private static final HashMap<String, Object> BEAN_CLASS_MAP = new HashMap<String, Object>();

    static {
        BEAN_CLASS_MAP.put(Mother.class.getName(), Mother.class);
        BEAN_CLASS_MAP.put(Son.class.getName(), Son.class);
        BEAN_CLASS_MAP.put(Daughter.class.getName(), Daughter.class);
    }

    /**
     * 作为一个bean的容器，其中的bean为单例
     * <p>
     * <key,value>=<beanId, Object>
     */
    private static final ConcurrentHashMap<String, Object> BEAN_MAP = new ConcurrentHashMap<>();

    /**
     * 锁对象
     */
    private static Object lock = new Object();

    /**
     * 根据class获取对应的bean
     */
    public static <T> T getBean(Class<T> clazz) {

        Object bean = BEAN_MAP.get(clazz.getName());
        if (null != bean) {
            return (T) bean;
        }

        //
        if (null == BEAN_CLASS_MAP.get(clazz.getName())) {
            throw new IllegalArgumentException("非法的bean class");
        }
        synchronized (lock) {
            T t = null;
            try {
                t = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            BEAN_MAP.put(clazz.getName(), t);
            return t;
        }
    }

    public static void main(String[] args) {
        Mother mother = ContainerSingleton.getBean(Mother.class);
        Mother mother1 = ContainerSingleton.getBean(Mother.class);

        System.out.println(mother);
        System.out.println(mother1);
    }

}

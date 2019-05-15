package com.coy.gupaoedu.study.pattern.singleton;

import java.io.Serializable;

/**
 * 饿汉单例模式
 * 在类加载时就完成了初始化，所以类加载较慢，但获取对象的速度快
 *
 * @author chenck
 * @date 2019/3/19 19:43
 */
public class EagerSingleton implements Serializable {

    /**
     * 静态私有成员，已初始化
     */
    private static EagerSingleton instance = new EagerSingleton();

    private EagerSingleton() {
        // 可防止通过反射的方式来获取该类的实例
        if (null != instance) {
            throw new RuntimeException("非法获取EagerSingleton");
        }
    }

    /**
     * 静态，不用同步（类加载时已初始化，不会有多线程的问题）
     */
    public static EagerSingleton getInstance() {
        return instance;
    }

    /**
     * 防止序列化生成新的实例
     */
    private Object readResolve() {
        System.out.println("readResolve()防止序列化生成新的实例");
        return instance;
    }


    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

package com.coy.gupaoedu.study.pattern.singleton;


/**
 * 懒汉式单例模式
 * <p>
 * 在类加载时，不创建实例，因此类加载速度快，但运行时获取对象的速度慢
 *
 * @author chenck
 * @date 2019/3/19 19:39
 */
public class LazySingleton {

    /**
     * 静态私用成员，没有初始化
     */
    private static LazySingleton lazySingleton = null;

    /**
     * 私有构造函数
     */
    private LazySingleton() {
        // 问题：通过反射可破坏单例（因为反射会调用构造函数）
        // 处理：构造函数中对实例判空，可防止通过反射的方式来获取该类的实例
        if (null != lazySingleton) {
            throw new RuntimeException("非法获取EagerSingleton");
        }
    }

    /**
     * 公开的静态，不同步，多线程环境下存在并发问题
     */
    public static LazySingleton getInstance1() {
        if (null == lazySingleton) {
            lazySingleton = new LazySingleton();
        }
        return lazySingleton;
    }

    /**
     * 方式二
     * 公开的静态，同步方法
     * <p>
     * 注：同步会影响系统性能的瓶颈和增加了额外的开销
     */
    public static synchronized LazySingleton getInstance2() {
        if (null == lazySingleton) {
            lazySingleton = new LazySingleton();
        }
        return lazySingleton;
    }

}

package com.coy.gupaoedu.study.pattern.singleton;


/**
 * Double Checked Lock懒汉式单例模式（双重检查锁）
 * <p>
 * 由于Java编译器和JIT的优化的原因系统无法保证我们期望的执行次序。
 * <p>
 * 在java5.0修改了内存模型,使用volatile声明的变量可以强制屏蔽编译器和JIT的优化工作
 *
 * @author chenck
 * @date 2019/3/19 19:39
 */
public class LazyDCLSingleton {

    /**
     * 静态私用成员，没有初始化
     * 注：通过volatile禁止指令重排序，解决双重检查锁的问题
     */
    private volatile static LazyDCLSingleton lazySingleton = null;

    /**
     * 私有构造函数
     */
    private LazyDCLSingleton() {
        // 问题：通过反射可破坏单例（因为反射会调用构造函数）
        // 处理：构造函数中对实例判空，可防止通过反射的方式来获取该类的实例
        if (null != lazySingleton) {
            throw new RuntimeException("非法获取EagerSingleton");
        }
    }

    /**
     * 双重检查锁:存在缺陷
     * JVM规范规定，指令重排序可以在不影响单线程程序执行结果前提下进行。
     */
    public static LazyDCLSingleton getInstance3() {
        if (null != lazySingleton) {
            return lazySingleton;
        }
        // 此处虽然加了同步，但还是存在小概率会出现问题
        synchronized (LazyDCLSingleton.class) {
            if (null == lazySingleton) {
                // lazySingleton = new LazySingleton();不是原子操作，JVM实际做了如下3个步骤
                // 1、分配lazySingleton对象的内存空间
                // 2、初始化对象LazySingleton（构造函数来初始化成员变量）
                // 3、设置singleton对象指向刚分配的内存空间
                // 但是在 JVM 的即时编译器中存在指令重排序的优化。也就是说上面的第二步和第三步的顺序是不能保证的，最终的执行顺序可能是 1-2-3 也可能是 1-3-2。
                // 如果是后者，则在 3 执行完毕、2 未执行之前，被线程二抢占了，这时 instance 已经是非 null 了（但却没有初始化），
                // 所以线程二会直接返回 instance，然后使用，然后顺理成章地报错。

                // 解决方案：JDK1.5之后给lazySingleton加上 volatile禁止指令重排序
                lazySingleton = new LazyDCLSingleton();
            }
        }
        return lazySingleton;
    }

}

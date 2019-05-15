package com.coy.gupaoedu.study.pattern.singleton;

/**
 * 静态内部类单例模式
 *
 * @author chenck
 * @date 2019/3/19 20:17
 */
public class StaticInnerClassSingleton {

    private StaticInnerClassSingleton() {
        if (null != InnerClass.instance) {
            throw new IllegalStateException("非法获取StaticInnerClassSingleton");
        }
    }

    /**
     * 静态内部类，实现延迟加载
     * InnerClass定义为private，所以InnerClass只可以被StaticInnerClassSingleton访问，从而完全隐藏实现的细节
     * 1、由于在调用 InnerClass.instance 的时候，才会对单例进行初始化，而且通过反射，是不能从外部类获取内部类的属性的。
     * 所以这种形式，很好的避免了反射入侵。
     * 2、由于静态内部类的特性，只有在其被第一次引用的时候才会被加载，所以可以保证其线程安全性。
     * 3、外部类可以访问内部类的所有方法与属性，包括私有方法与属性
     */
    private static class InnerClass {
        protected static StaticInnerClassSingleton instance = new StaticInnerClassSingleton();
    }

    public static StaticInnerClassSingleton getInstance() {
        return InnerClass.instance;
    }
}

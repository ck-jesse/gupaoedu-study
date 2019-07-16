package com.coy.gupaoedu.study.jvm.gcroot;

/**
 * 【GC Root 的对象】方法区中常量引用的对象
 *
 * @author chenck
 * @date 2019/7/16 10:19
 */
public class MethodAreaStaicFinalProperties {
    /**
     * 常量m
     */
    public static final MethodAreaStaicFinalProperties m = new MethodAreaStaicFinalProperties("final");

    public MethodAreaStaicFinalProperties(String name) {
    }

    /**
     * m 即为方法区中的常量引用，也为 GC Root，s 置为 null 后，final 对象也不会因没有与 GC Root 建立联系而被回收。
     */
    public static void main(String[] args) {
        MethodAreaStaicProperties s = new MethodAreaStaicProperties("staticProperties");
        s = null;
        System.gc();
    }
}

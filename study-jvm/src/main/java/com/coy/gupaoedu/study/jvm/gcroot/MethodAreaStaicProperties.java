package com.coy.gupaoedu.study.jvm.gcroot;

/**
 * 【GC Root 的对象】方法区中类静态属性引用的对象
 *
 * @author chenck
 * @date 2019/7/16 10:14
 */
public class MethodAreaStaicProperties {

    /**
     * 静态变量m
     */
    public static MethodAreaStaicProperties m;

    public MethodAreaStaicProperties(String name) {
    }

    /**
     * s 为 GC Root，s 置为 null，经过 GC 后，s 所指向的 properties 对象由于无法与 GC Root 建立关系被回收。
     * 而 m 作为类的静态属性，也属于 GC Root，parameter 对象依然与 GC root 建立着连接，所以此时 parameter 对象并不会被回收。
     */
    public static void main(String[] args) {
        MethodAreaStaicProperties s = new MethodAreaStaicProperties("properties");
        s.m = new MethodAreaStaicProperties("parameter");
        s = null;
        System.gc();
    }
}

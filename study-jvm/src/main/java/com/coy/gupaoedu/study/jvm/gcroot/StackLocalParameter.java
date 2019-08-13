package com.coy.gupaoedu.study.jvm.gcroot;

/**
 * 【GC Root 的对象】虚拟机栈（栈帧中的本地变量表）中引用的对象
 *
 * @author chenck
 * @date 2019/7/16 10:12
 */
public class StackLocalParameter {

    public StackLocalParameter(String name) {

    }

    /**
     * 此时的 s，即为 GC Root，当s置空时，localParameter 对象也断掉了与 GC Root 的引用链，将被回收。
     */
    public static void main(String[] args) {
        StackLocalParameter s = new StackLocalParameter("localParameter");
        s = null;
        System.gc();
    }

    /*
    ParNew : 年轻代垃圾收集器，多线程，采用标记—复制算法。
    CMS：老年代的收集器，全称（Concurrent Mark and Sweep），是一种以获取最短回收停顿时间为目标的收集器。
     */

    /**
     -XX:+PrintGC 输出GC日志
     -XX:+PrintGCDetails 输出GC的详细日志
     -XX:+PrintGCTimeStamps 输出GC的时间戳（以基准时间的形式）
     -XX:+PrintGCDateStamps 输出GC的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）
     -XX:+PrintHeapAtGC 在进行GC的前后打印出堆的信息
     -Xloggc:../logs/gc.log 日志文件的输出路径
     */
}

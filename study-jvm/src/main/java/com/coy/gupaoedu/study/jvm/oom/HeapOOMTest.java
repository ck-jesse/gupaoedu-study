package com.coy.gupaoedu.study.jvm.oom;

import com.coy.gupaoedu.study.jvm.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 堆内存溢出
 * <p>
 * 【重点】结合  jvisualvm.exe 的 Visual GC 插件来分析 Metaspace/Old/Eden/Survisor 各个内存区域的GC情况
 *
 * @author chenck
 * @date 2020/8/3 16:14
 */
public class HeapOOMTest {

    List<User> list = new ArrayList<>();

    public void add(User user) {
        list.add(user);
    }

    /**
     * 设置堆内存大小： -Xmx10M -Xms10M -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc.log -XX:+UseConcMarkSweepGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=heap.hprof
     * <p>
     * 垃圾收集器参数配置
     * -XX:+UseParallelGC
     * -XX:+UseConcMarkSweepGC
     * -XX:+UseG1GC
     * <p>
     * 异常：
     * java.lang.OutOfMemoryError: Java heap space 堆溢出
     * java.lang.OutOfMemoryError: GC overhead limit exceeded 达到GC的开销限制，也就是说JVM在执行GC时花费了太多时间
     */
    public static void main(String[] args) throws InterruptedException {
        HeapOOMTest test = new HeapOOMTest();
        while (true) {
            test.add(new User("heapoomtestssssssssssssssssssssssssssssssss"));
//            Thread.sleep(1);
        }
    }
}

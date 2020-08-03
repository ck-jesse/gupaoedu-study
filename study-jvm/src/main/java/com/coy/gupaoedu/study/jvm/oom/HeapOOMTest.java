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
     * 设置堆内存大小： -Xmx10M -Xms10M -XX:+PrintGCDetails -XX:+PrintGCDateStamps
     * <p>
     * 异常：java.lang.OutOfMemoryError: GC overhead limit exceeded
     */
    public static void main(String[] args) throws InterruptedException {
        HeapOOMTest test = new HeapOOMTest();
        while (true) {
            test.add(new User("heapoomtestssssssssssssssssssssssssssssssss"));
            Thread.sleep(1);
        }
    }
}

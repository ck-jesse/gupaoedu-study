package com.coy.gupaoedu.study.jvm.oom;

import com.coy.gupaoedu.study.jvm.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenck
 * @date 2021/7/14 10:34
 */
public class GCLogTest {

    List<User> list = new ArrayList<>();

    public void add(User user) {
        list.add(user);
    }

    /**
     * # 必备
     * -XX:+PrintGCDetails
     * -XX:+PrintGCDateStamps
     * -XX:+PrintTenuringDistribution // 打印对象分布,分析 GC 时的**晋升情况和晋升导致的高暂停**
     * -XX:+PrintHeapAtGC // GC 后打印堆数据,对比一下 GC 前后的堆内存情况
     * -XX:+PrintReferenceGC // 打印 Reference 处理信息,可用于分析强引用/弱引用/软引用/虚引用/finalize方法
     * -XX:+PrintGCApplicationStoppedTime   // 打印 STW 时间
     * <p>
     * # GC日志输出的文件路径
     * -Xloggc:/path/to/gc-%t.log
     * # 开启日志文件分割
     * -XX:+UseGCLogFileRotation
     * # 最多分割几个文件，超过之后从头文件开始写
     * -XX:NumberOfGCLogFiles=14
     * # 每个文件上限大小，超过就触发分割
     * -XX:GCLogFileSize=100M
     * 注：配置时间戳作文 GC 日志文件名的同时，也配置JVM的GC日志分割策略。
     * <p>
     * -Xmx10M -Xms10M -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintReferenceGC -XX:+PrintGCApplicationStoppedTime -Xloggc:E:\temp\gc-%t.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=14 -XX:GCLogFileSize=100M -XX:+UseConcMarkSweepGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=E:\temp\heap-%t.hprof
     */
    public static void main(String[] args) throws InterruptedException {
        HeapOOMTest test = new HeapOOMTest();
        while (true) {
            test.add(new User("heapoomtestssssssssssssssssssssssssssssssss"));
            Thread.sleep(1);
        }
    }
}

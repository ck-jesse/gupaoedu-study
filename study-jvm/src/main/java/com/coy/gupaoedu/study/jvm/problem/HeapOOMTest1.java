package com.coy.gupaoedu.study.jvm.problem;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟堆内存溢出
 * <p>
 *
 * @author chenck
 * @date 2020/8/3 16:14
 */
public class HeapOOMTest1 {

    List<byte[]> list = new ArrayList<>();

    public void add(byte[] bytes) {
        list.add(bytes);
    }

    /**
     * 配置堆内存大小，打印GC日志，CMS收集器，自动转储DUMP文件
     * -Xmx128M -Xms128M -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:e:\temp\gc_%t.log -XX:+UseConcMarkSweepGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=e:\temp\dump
     */
    public static void main(String[] args) throws InterruptedException {
        HeapOOMTest1 test = new HeapOOMTest1();
        while (true) {
            byte[] bytes = new byte[1024 * 1024];// 1M
            test.add(bytes);
        }
    }
}

package com.coy.gupaoedu.study.jvm.reference;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * ReferenceQueue 是引用队列，用于存放待回收的引用对象。
 * <p>
 *
 * <p>
 * 对于软引用、弱引用和虚引用，如果我们希望当一个对象被垃圾回收器回收时能得到通知，进行额外的处理，这时候就需要使用到引用队列了。
 * 在一个对象被垃圾回收器扫描到将要进行回收时，其相应的引用包装类，即reference对象会被放入其注册的引用队列queue中。
 * 可以从queue中获取到相应的对象信息，同时进行额外的处理。比如反向操作，数据清理，资源释放等。
 * <p>
 * ReferenceQueue可用来保存需要关注的Reference队列
 * ReferenceQueue内部实现实际上是一个栈
 * ReferenceQueue可用来进行数据监控，资源释放等
 * <p>
 * 基础概念：
 * https://blog.csdn.net/gdutxiaoxu/article/details/80738581
 * <p>
 * 实际应用：
 * 参见Guava Cache 之 com.google.common.cache.LoadingCache#get(java.lang.Object)
 *
 * @author chenck
 * @date 2020/3/20 17:06
 */
public class ReferenceQueueTest {

    private static int _1M = 1024 * 1024;

    /**
     * 虚拟机大小设置： -Xms10m -Xmx20m
     * <p>
     * 当检测到对象的可到达性更改时，垃圾回收器将已注册的引用对象添加到队列中，ReferenceQueue实现了入队（enqueue）和出队（poll），还有remove操作，内部元素head就是泛型的Reference。
     */
    public static void main(String[] args) throws InterruptedException {
        ReferenceQueue<byte[]> referenceQueue = new ReferenceQueue<>();
        Map<Object, MyWeakReference> hashMap = new HashMap<>();

        // 创建一个线程，使用死循环来从引用队列中获取元素，监控对象被回收的状态。
        Thread thread = new Thread(() -> {
            try {
                int n = 0;
                MyWeakReference k;
                // 这里通过referenceQueue监控到有引用被回收后，通过hashMap反向获取到对应的value，然后进行资源释放等。
                while ((k = (MyWeakReference) referenceQueue.remove()) != null) {
                    System.out.println((++n) + "回收了:" + k);
                    hashMap.remove(k.key);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();

        // 循环往map中添加100个映射关系
        for (int i = 0; i < 100; i++) {
            Object key = new Object();
            byte[] value = new byte[_1M];
            // 通过配合使用ReferenceQueue，可以较好的监控对象的生存状态。
            hashMap.put(key, new MyWeakReference(key, value, referenceQueue));
        }
        System.out.println("map.size->" + hashMap.size());
        Thread.sleep(1000);
        System.out.println("map.size->" + hashMap.size());

        int aliveNum = 0;
        for (Map.Entry<Object, MyWeakReference> entry : hashMap.entrySet()) {
            if (entry != null) {
                if (entry.getValue().get() != null) {
                    aliveNum++;
                }
            }
        }
        System.out.println("map中存活的对象数量：" + aliveNum);
    }

    /**
     * 自定义弱引用
     */
    static class MyWeakReference extends WeakReference<byte[]> {
        private Object key;

        MyWeakReference(Object key, byte[] referent, ReferenceQueue<? super byte[]> referenceQueue) {
            super(referent, referenceQueue);
            this.key = key;
        }
    }
}

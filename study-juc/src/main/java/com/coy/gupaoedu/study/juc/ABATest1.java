package com.coy.gupaoedu.study.juc;

import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * CAS中的 ABA 问题
 * 在CAS算法中，需要取出内存中某时刻的数据（由用户完成），在下一时刻比较并替换（由CPU完成，该操作是原子的）。这个时间差中，会导致数据的变化。
 * 假设如下事件序列：
 * <p>
 * 1.线程1 从内存位置V中取出A。
 * 2.线程2 从内存位置V中取出A。
 * 3.线程2 进行了一些操作，将B写入位置V。
 * 4.线程2 将A再次写入位置V。
 * 5.线程1 进行CAS操作，发现位置V中仍然是A，操作成功。
 * 尽管线程1 的CAS操作成功，但不代表这个过程没有问题——对于线程1而言线程2的修改已经丢失。
 * <p>
 * 解决方案：通过加入版本号来解决
 *
 * @author chenck
 * @date 2019/8/9 22:26
 */
public class ABATest1 {

    public static void main(String[] args) {

        String oldValue = "aba";
        String newValue = "aba22";

        // 更加简单的描述是与否的问题
        // 设置初始值为aba，初始标识为false
        AtomicMarkableReference<String> money = new AtomicMarkableReference<>(oldValue, false);

        for (int i = 0; i < 3; i++) {
            new Thread("updateThread-" + (i + 1)) {
                @Override
                public void run() {
                    while (true) {
                        // 比较并设置新值，当期望标识为false是，才进行设置
                        if (money.compareAndSet(oldValue, newValue, false, true)) {
                            System.out.println(Thread.currentThread().getName() + " 原值=" + oldValue + "，新值=" + money.getReference());
                            break;
                        }
                    }
                }
            }.start();
        }
    }

}

package com.coy.gupaoedu.study.juc;

import java.util.concurrent.atomic.AtomicStampedReference;

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
public class ABATest {

    public static void main(String[] args) {

        // 设置初始值为20，版本号为0
        AtomicStampedReference<Integer> money = new AtomicStampedReference<>(19, 0);

        for (int i = 0; i < 3; i++) {
            // 获取某一个时刻的版本号
            int stamp = money.getStamp();
            System.out.println("stamp的值是" + stamp);

            // 充值线程
            new Thread("rechargeThread-" + (i + 1)) {
                @Override
                public void run() {
                    while (true) {
                        Integer account = money.getReference();
                        if (account < 20) {
                            // 比较并设置新值
                            if (money.compareAndSet(account, account + 20, stamp, stamp + 1)) {
                                System.out.println(Thread.currentThread().getName() + " 余额" + account + "小于20元，【充值成功】，目前余额：" + money.getReference() + "元");
                                break;
                            }
                        } else {
                            System.out.println(Thread.currentThread().getName() + " 余额" + account + "大于20元,无需充值");
                        }
                    }
                }
            }.start();
        }
        // 消费线程，主要目的是将值修改为小于20的值，以便在上面的充值线程中验证是否会多次充值
        new Thread("consumeThread") {
            @Override
            public void run() {
                for (int j = 0; j < 100; j++) {
                    while (true) {
                        int timeStamp = money.getStamp();//1
                        int currentMoney = money.getReference();//39
                        if (currentMoney > 10) {
                            System.out.println(Thread.currentThread().getName() + " ==> 当前账户余额"+currentMoney+"大于10元");
                            if (money.compareAndSet(currentMoney, currentMoney - 10, timeStamp, timeStamp + 1)) {
                                System.out.println(Thread.currentThread().getName() + " ==> 消费者成功消费10元，余额" + money.getReference());
                                break;
                            }
                        } else {
                            System.out.println(Thread.currentThread().getName() + " ==> 金额不足10元");
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            break;
                        }
                    }
                }
            }
        }.start();
    }

}

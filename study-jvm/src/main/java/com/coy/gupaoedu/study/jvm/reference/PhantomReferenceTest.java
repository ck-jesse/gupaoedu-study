package com.coy.gupaoedu.study.jvm.reference;

import com.coy.gupaoedu.study.jvm.User;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * 虚引用
 * 虚引用是所有引用中最弱的一个持有一个虚引用的对象，和没有引用一样，随时都有可能会被垃圾回收器回收，
 * 当用虚引用的get方法去尝试获得强引用对象时总是会失败，并且他必须和引用队列一起使用，用于跟踪垃圾回收过程，
 * 当垃圾回收器回收一个持有虚引用的对象时，在回收对象后，将这个虚引用对象加入到引用队列中，用来通知应用程序垃圾的回收情况。
 *
 * @author chenck
 * @date 2020/3/20 17:03
 */
public class PhantomReferenceTest {

    /**
     * 由于虚引用可以跟踪对象的回收时间，所以可以将一些资源的释放操作放置在虚引用中执行和记录
     */
    public static void main(String[] args) {
        User u = new User("吉米");
        ReferenceQueue<? super User> queue = new ReferenceQueue<User>();
        PhantomReference<User> pr = new PhantomReference<User>(u, queue);
        System.out.println(pr.get());// 输出为null
    }
}

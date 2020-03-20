package com.coy.gupaoedu.study.jvm.reference;

import com.coy.gupaoedu.study.jvm.User;

import java.lang.ref.WeakReference;

/**
 * 弱引用
 * 弱引用与软引用的区别在于：只具有弱引用的对象拥有更短暂的生命周期。
 * 在垃圾回收器线程扫描它所管辖的内存区域的过程中，一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。
 * 不过，由于垃圾回收器是一个优先级很低的线程，因此不一定会很快发现那些只具有弱引用的对象。
 * 弱引用可以和一个引用队列（ReferenceQueue）联合使用，如果弱引用所引用的对象被垃圾回收，Java虚拟机就会把这个弱引用加入到与之关联的引用队列中。
 *
 * @author chenck
 * @date 2020/3/20 16:55
 */
public class WeakReferenceTest {

    /**
     * 弱引用的对象，不管当前内存空间足够与否，都会回收它的内存
     * 弱引用是比软引用还弱的引用，在系统进行GC 时，只要发现弱引用，不管系统的堆空间是用了一点还是用了一大半，都会回收弱引用的对象。
     */
    public static void main(String[] args) {
        User user = new User();
        WeakReference<User> weakReference = new WeakReference(user);
        user = null;
        System.out.println(weakReference.get());
    }
}

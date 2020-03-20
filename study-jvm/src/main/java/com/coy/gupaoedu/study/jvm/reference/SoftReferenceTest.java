package com.coy.gupaoedu.study.jvm.reference;

import com.coy.gupaoedu.study.jvm.User;
import org.junit.Test;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

/**
 * 软引用
 * 如果一个对象只具有软引用，则内存空间足够，垃圾回收器就不会回收它； 如果内存空间不足了，就会回收这些对象的内存。
 * 只要垃圾回收器没有回收它，该对象就可以被程序使用。 软引用可用来实现内存敏感的高速缓存（下文给出示例）。
 * 软引用可以和一个引用队列（ReferenceQueue）联合使用，如果软引用所引用的对象被垃圾回收器回收，Java虚拟机就会把这个软引用加入到与之关联的引用队列中。
 *
 * @author chenck
 * @date 2020/3/20 16:10
 */
public class SoftReferenceTest {

    /**
     * 软引用测试
     * 对于软引用关联的对象GC未必会一定会收，只有当内存资源紧张时，软引用对象才会被回收，所以软引用对象不会引起内存溢出(OOM)
     * 软引用主要用户实现类似缓存的功能，在内存足够的情况下直接通过软引用取值，无需从繁忙的真实来源查询数据，提升速度；当内存不足时，自动删除这部分缓存数据，从真正的来源查询这些数据。
     * <p>
     * 如果一个对象只有软引用，则在内存充足的情况下是不会回收此对象的，但是，在内部不足即将要抛出OOM异常时就会回收此对象来解决内存不足的问题。
     * 这也就说明了当内存充足的时候一个对象只有软引用也不会被JVM回收。
     */
    @Test
    public void softReferenceTest() throws InterruptedException {
        // 创建一个User对象，也就是强引用
        User user = new User("coy");

        // 对于User对象，有两个引用路径，一个是来自SoftReference对象的软引用，一个来自变量User的强引用，所以这个User对象是强可及对象。
        SoftReference softReference = new SoftReference(user);

        // 回收前，通过SoftReference对象重新获得对该实例的强引用。
        System.out.println(softReference.get() != null);

        // 随即，我们可以结束user对这个User实例的强引用。此时，这个User对象成为了软可及对象。
        // 如果垃圾收集线程进行内存垃圾收集，并不会因为有一个SoftReference对该对象的引用而始终保留该对象。
        // Java虚拟机的垃圾收集线程对软可及对象和其他一般Java对象进行了区别对待:软可及对象的清理是由垃圾收集线程根据其特定算法按照内存需求决定的。
        // 也就是说，垃圾收集线程会在虚拟机抛出OutOfMemoryError之前回收软可及对象，而且虚拟机会尽可能优先回收长时间闲置不用的软可及对象，
        // 对那些刚刚构建的或刚刚使用过的“新”软可反对象会被虚拟机尽可能保留。
        user = null;

        // 触发gc，但不一定会执行
        System.gc();

        // 真正回收后，通过SoftReference.get()只能获取到null了
        System.out.println(softReference.get() != null);
    }

    /**
     * 清除软引用对象测试
     * <p>
     * 作为一个Java对象，SoftReference对象除了具有保存软引用的特殊性之外，也具有Java对象的一般性。
     * 所以，当软可及对象被回收之后，虽然这个SoftReference对象的get()方法返回null,
     * 但这个SoftReference对象已经不再具有存在的价值，需要一个适当的清除机制，避免大量SoftReference对象带来的内存泄漏。
     * 那么可以使用 ReferenceQueue 清除失去了软引用对象的SoftReference对象
     */
    @Test
    public void softReferenceCleanTest() {
        User user = new User();

        // 创建ReferenceQueue队列
        ReferenceQueue referenceQueue = new ReferenceQueue();

        // 创建软引用对象，传入user强可及对象 和 ReferenceQueue队列
        SoftReference softReference = new SoftReference(user, referenceQueue);

        // 那么，当这个SoftReference所软引用的user被垃圾收集器回收的同时，softReference所强引用的SoftReference对象被列入ReferenceQueue。
        // 也就是说，ReferenceQueue中保存的对象是Reference对象，而且是已经失去了它所软引用的对象的Reference对象。
        // 另外从ReferenceQueue这个名字也可以看出，它是一个队列，当我们调用它的poll()方法的时候，如果这个队列中不是空队列，那么将返回队列前面的那个Reference对象。
        // 在任何时候，我们都可以调用ReferenceQueue的poll()方法来检查是否有它所关心的非强可及对象被回收。
        // 如果队列为空，将返回一个null,否则该方法返回队列中前面的一个Reference对象。
        // 利用这个方法，我们可以检查哪个SoftReference所软引用的对象已经被回收。于是我们可以把这些失去所软引用的对象的SoftReference对象清除掉。
        SoftReference ref = null;
        while ((ref = (SoftReference) referenceQueue.poll()) != null) {
            System.out.println("清除：" + ref.toString());
            // 清除ref
            ref = null;
        }

    }
}

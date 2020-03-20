package com.coy.gupaoedu.study.jvm.reference;

import com.coy.gupaoedu.study.jvm.User;

/**
 * 强引用
 * 强引用是使用最普遍的引用。
 * 如果一个对象具有强引用，那垃圾回收器绝不会回收它。
 * 当内存空间不足，Java虚拟机宁愿抛出OutOfMemoryError错误，使程序异常终止，也不会靠随意回收具有强引用的对象来解决内存不足的问题。
 * ps：强引用其实也就是我们平时A a = new A()这个意思。
 *
 * @author chenck
 * @date 2020/3/20 16:15
 */
public class StrongReferenceTest {

    public static void main(String[] args) {
        // 创建一个User对象，也就是强引用
        User user = new User();
    }
}

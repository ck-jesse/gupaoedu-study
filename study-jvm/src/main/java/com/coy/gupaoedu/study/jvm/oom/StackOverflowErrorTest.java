package com.coy.gupaoedu.study.jvm.oom;

/**
 * 虚拟机栈 - 线程栈溢出测试 java.lang.StackOverflowError
 *
 * @author chenck
 * @date 2019/12/8 21:42
 */
public class StackOverflowErrorTest {

    /**
     * 每一次方法的调用，都会在线程栈中入栈一个方法栈帧
     * 因循环调用，方法栈帧只有入栈，没有出栈，所以报 栈溢出异常 java.lang.StackOverflowError
     */
    public static void method(int count) {
        System.out.println(count++);
        // 循环调用 method()
        method(count);
    }

    public static void main(String[] args) {
        method(1);
    }
}

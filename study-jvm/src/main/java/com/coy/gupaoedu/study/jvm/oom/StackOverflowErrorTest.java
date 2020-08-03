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
        // Stack Space用来做方法的递归调用时压入Stack Frame(栈帧)。所以当递归调用太深的时候，就有可能耗尽Stack Space，爆出StackOverflow的错误。
        method(count);
    }

    /**
     * 设置栈大小： -Xss128k -XX:+PrintGCDetails -XX:+PrintGCDateStamps
     * 异常： java.lang.StackOverflowError
     * -Xss128k：设置每个线程的堆栈大小。JDK 5以后每个线程堆栈大小为1M，以前每个线程堆栈大小为256K。根据应用的线程所需内存大小进行调整。
     * 在相同物理内存下，减小这个值能生成更多的线程。但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。
     * 线程栈的大小是个双刃剑，如果设置过小，可能会出现栈溢出，特别是在该线程内有递归、大的循环时出现溢出的可能性更大，
     * 如果该值设置过大，就有影响到创建栈的数量，如果是多线程的应用，就会出现内存溢出的错误。
     */
    public static void main(String[] args) {
        method(1);
    }
}

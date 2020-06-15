package com.coy.gupaoedu.study.jdk8.functional;

import com.coy.gupaoedu.study.jdk8.Student;

import java.util.function.BiFunction;

/**
 * java.util.function.Function<T, R> 表示接受两个参数并返回结果的函数
 *
 * @author chenck
 * @date 2020/6/10 12:19
 */
public class BiFunctionTest {

    private String name;
    private String addr;

    public BiFunctionTest(String name, String addr) {
        this.name = name;
        this.addr = addr;
    }

    public BiFunctionTest(String name) {
        this.name = name;
    }

    public Student function(BiFunction<String, String, Student> function) {
        return function.apply(this.name, this.addr);
    }

    public static void main(String[] args) {
        BiFunctionTest test = new BiFunctionTest("hello");

        Student student = test.function((s, s2) -> {
            System.out.println("BiFunction.apply() param = " + s + ",addr=" + s2);
            return new Student(s);
        });
        System.out.println(student.toString());
    }
}

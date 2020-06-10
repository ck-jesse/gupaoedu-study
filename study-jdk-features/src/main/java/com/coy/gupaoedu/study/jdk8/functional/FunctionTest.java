package com.coy.gupaoedu.study.jdk8.functional;

import com.coy.gupaoedu.study.jdk8.Student;

import java.util.function.Function;

/**
 * java.util.function.Function<T, R> 表示接受一个参数并返回结果的函数
 *
 * @author chenck
 * @date 2020/6/10 12:19
 */
public class FunctionTest {

    private String name;

    public FunctionTest(String name) {
        this.name = name;
    }

    public Student function(Function<String, Student> function) {
        return function.apply(this.name);
    }

    public static void main(String[] args) {
        FunctionTest test = new FunctionTest("hello");

        Student student = test.function(s -> {
            System.out.println("Function.apply() param = " + s);
            return new Student(s);
        });
        System.out.println(student.toString());
    }
}

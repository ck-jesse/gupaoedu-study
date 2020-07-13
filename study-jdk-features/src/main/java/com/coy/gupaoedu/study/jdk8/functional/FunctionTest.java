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

        // function.apply 执行前执行
        function = function.compose(o -> {
            System.out.println("Function.compose() " + o);
            return o;
        });

        // function.apply 执行后执行
        function = function.andThen(student -> {
            System.out.println("Function.andThen() " + student.toString());
            return student;
        });

        return function.apply(this.name);
    }

    /**
     * 还原 compose 和 andThen 的实现，以便阅读和理解
     *
     * @see java.util.function.Function#compose(java.util.function.Function)
     * @see java.util.function.Function#andThen(java.util.function.Function)
     */
    public Student function1(Function<String, Student> function) {
        // function.apply 执行前执行
        // 还原Function.compose()的实现
        Function<String, String> before = new Function<String, String>() {
            @Override
            public String apply(String o) {
                System.out.println("Function.before() " + o);
                return o;
            }
        };
        Function<String, Student> finalFunction = function;
        function = new Function<String, Student>() {
            @Override
            public Student apply(String s) {
                return finalFunction.apply(before.apply(s));
            }
        };

        // function.apply 执行后执行
        // 还原Function.andThen()的实现
        Function<Student, Student> after = new Function<Student, Student>() {
            @Override
            public Student apply(Student student) {
                System.out.println("Function.after() " + student.toString());
                return student;
            }
        };
        Function<String, Student> finalFunction1 = function;
        function = new Function<String, Student>() {
            @Override
            public Student apply(String s) {
                Student student = after.apply(finalFunction1.apply(s));
                return student;
            }
        };

        return function.apply(this.name);
    }

    public static void main(String[] args) {
        FunctionTest test = new FunctionTest("hello");

        Student student = test.function(s -> {
            System.out.println("Function.apply() param = " + s);
            return new Student(s);
        });
        System.out.println(student.toString());
        System.out.println();

        student = test.function1(s -> {
            System.out.println("Function.apply() param = " + s);
            return new Student(s);
        });
        System.out.println(student.toString());
    }
}

package com.coy.gupaoedu.study.reflect;

import java.lang.reflect.Method;

/**
 * 通过反射执行method的测试
 *
 * @author chenck
 * @date 2020/5/27 20:03
 */
public class ReflectMethodTest {

    public static void main(String[] args) throws Exception {

        // 通过反射执行getAddr()时，需传入User实例
        User user = new User();
        user.setAddr("addr");
        Method method = User.class.getMethod("getAddr");
        Object value = method.invoke(user);
        System.out.println(value);

        // 通过反射执行静态方法getInstance()时，需传入User.class，而不是User实例
        method = User.class.getMethod("getInstance");
        value = method.invoke(User.class);
        System.out.println(value);
    }
}

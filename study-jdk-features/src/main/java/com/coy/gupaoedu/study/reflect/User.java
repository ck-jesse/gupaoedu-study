package com.coy.gupaoedu.study.reflect;

/**
 * @author chenck
 * @date 2020/5/27 20:03
 */
public class User {

    private String name;
    private String addr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 通过反射执行getAddr()时，需传入User实例
    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    // 通过反射执行静态方法getInstance()时，需传入User.class，而不是User实例
    private static User user = new User();

    public static User getInstance() {
        return user;
    }
}

package com.coy.gupaoedu.study.pattern.proxy.staticproxy;


import com.coy.gupaoedu.study.pattern.proxy.UserServiceImpl;

/**
 * 静态代理：显式声明被代理对象
 *
 * @author chenck
 * @date 2019/3/20 21:33
 */
public class StaticProxyTest {

    public static void main(String[] args) {
        UserServiceStaticProxy proxy = new UserServiceStaticProxy(new UserServiceImpl());
        proxy.printUserInfo("用户名");
    }

}

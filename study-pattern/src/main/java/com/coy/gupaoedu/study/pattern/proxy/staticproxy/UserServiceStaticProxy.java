package com.coy.gupaoedu.study.pattern.proxy.staticproxy;


import com.coy.gupaoedu.study.pattern.proxy.UserService;

/**
 * 静态代理：显式声明被代理对象
 * 由程序员创建或特定工具自动生成源代码，再对其编译
 * 
 * @author chenck
 * @date 2017年3月15日 下午4:06:31
 */
public class UserServiceStaticProxy implements UserService {

    private UserService userService;

    public UserServiceStaticProxy(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String printUserInfo(String name) {
        System.out.println("静态代理before");
        String result = userService.printUserInfo(name);
        System.out.println("静态代理after");
        return result;
    }

}

package com.coy.gupaoedu.study.pattern.proxy.dynamicproxy.cglib;

import com.coy.gupaoedu.study.pattern.proxy.UserService;
import com.coy.gupaoedu.study.pattern.proxy.UserServiceImpl;

/**
 * @author chenck
 * @date 2019/3/20 21:05
 */
public class CglibProxyTest {

    public static void main(String[] args) {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        // 日志代理
        UserService userService = (UserService) CglibProxyUtil.createProxyCglib(userServiceImpl,
                new LogProxyIntercepter());
        userService.printUserInfo("用户名");
    }
}

package com.coy.gupaoedu.study.pattern.proxy.dynamicproxy.jdk;

import com.coy.gupaoedu.study.pattern.proxy.UserService;
import com.coy.gupaoedu.study.pattern.proxy.UserServiceImpl;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;

/**
 * JDK动态代理测试入口
 * <p>
 * 代理模式：是指为其他对象提供一种代理，以控制对这个对象的访问
 * 代理对象在客服端和目标对象之间起到中介作用。
 * <p>
 * 适用场景：
 * 1、保护目标对象
 * 2、增强目标对象
 *
 * @author chenck
 * @date 2017年3月15日 下午4:01:27
 */
public class JdkProxyTest {

    public static void main(String[] args) throws Exception {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        // 日志代理
        UserService userService1 = (UserService) JdkProxyUtil.createProxy(userServiceImpl, new LogProxyHandler());
        System.out.println(userService1);
        System.out.println(userService1.getClass());
        userService1.printUserInfo("用户名");

        // 权限代理
        UserService userService2 = (UserService) JdkProxyUtil.createProxy(userServiceImpl, new AuthProxyHandler());
        System.out.println(userService2);
        System.out.println(userService1.getClass());
        userService2.printUserInfo("用户名");

        System.out.println("userServiceImpl的前后两个代理对象的比较：" + (userService1 == userService2));

        // 代理模式下：一个方法如果需要被两个InvocationHandler进行拦截处理的话，应该怎么实现呢？如上面的日志代理和权限代理

        // 将代理对象$Proxy0记录到文件中，以便查看分析
        byte[] bytes = ProxyGenerator.generateProxyClass("$Proxy0", new Class[]{UserService.class});
        FileOutputStream os = new FileOutputStream("D://develop//咕泡学院//$Proxy0.class");
        os.write(bytes);
        os.close();
    }

}

package com.coy.gupaoedu.study.pattern.proxy.dynamicproxy;

/**
 * @author chenck
 * @date 2019/3/20 21:39
 */
public class Readme {

    /**
     CGLib和JDK动态代理对比：
     1.JDK动态代理是实现了被代理对象的接口，CGLib是继承了被代理对象。
     2.JDK和CGLib都是在运行期生成字节码，JDK是直接写Class字节码，CGLib使用ASM框架写Class字节码，Cglib代理实现更复杂，生成代理类比JDK效率低。
     3.JDK调用代理方法，是通过反射机制调用，CGLib是通过FastClass机制直接调用方法，CGLib执行效率更高。

     代理模式的优点：
     代理模式能将代理对象与真实被调用的目标对象分离。
     一定程度上降低了系统的耦合程度，易于扩展。
     代理可以起到保护目标对象的作用。
     增强目标对象的职责。

     代理模式的缺点：
     代理模式会造成系统设计中类的数目增加。
     在客户端和目标对象之间增加了一个代理对象，会造成请求处理速度变慢。
     增加了系统的复杂度。
     */

    /**
     问：AOP是怎样在一个方法上进行多个拦截处理的？
     答：通过Invocation中维护一个拦截器列表来实现。实际可采用责任链模式来实现对一个方法使用多个拦截器的实现。

     */
}

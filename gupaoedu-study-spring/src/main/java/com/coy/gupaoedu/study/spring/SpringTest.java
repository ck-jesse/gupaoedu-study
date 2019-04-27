package com.coy.gupaoedu.study.spring;

import com.coy.gupaoedu.study.spring.demo.aspect.LogAspect;
import com.coy.gupaoedu.study.spring.demo.service.IDemoService;
import com.coy.gupaoedu.study.spring.demo.service.impl.DemoService;
import com.coy.gupaoedu.study.spring.demo.service.impl.UserService;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPJoinPoint;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPMethodInvocation;
import com.coy.gupaoedu.study.spring.framework.context.GPApplicationContext;
import com.coy.gupaoedu.study.spring.framework.context.support.GPAbstractApplicationContext;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author chenck
 * @date 2019/4/19 16:10
 */
public class SpringTest {

    /*
       1、加载beanDefinition时，对于接口也会创建BeanDefinition吗？
       答：有过滤掉接口和抽象类
       org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider.findCandidateComponents
       org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider.isCandidateComponent(org.springframework.beans.factory.annotation.AnnotatedBeanDefinition)
       判断是否为具体的class(也就是对于接口和抽象类返回false)
       org.springframework.core.type.StandardClassMetadata.isConcrete

       2、实例化时，是会将beanDefinitionMap中的所有的定义都创建对象吗？
       答：对于非抽象类、非延迟加载、且为单例模式的类进行创建

       3、是否对没有定义spring注解的类创建BeanDefinition？
       答：

       4、依赖注入时，字段类型定义为接口的字段，是怎么注入的？
       答：1)根据字段类型获取所有的beanNames

       5、aop创建代理对象以后，代理中的属性为null，怎么讲目标对象的属性赋值给代理对象呢？
       答：代理对象中含有目标对象的引用，虽然创建代理对象后看到的属性为null，但是在实际调用代理对象的方法时，为null的属性可以从目标对象中拿到对应的属性值，这个不知道是怎么获取到并赋值的？

       6、两个方法A和B被同一个拦截器拦截到，当A中调用B时，会B的拦截器执行完毕后，其中的Invocation已经赋值为了B的Invocation，所以在A重新拿到执行权限时，其获取到了InvocationB，所以出现了混乱
       答：可以在拦截器链的第一个位置添加一个Invocation拦截器(结合ThreadLocal)，具体的逻辑如下：
       1）从ThreadLocal中将旧的Invocation取出来，并记录下来
       2）设置当前Invocation到ThreadLocal中，保证当前的拦截器能获取到自己的Invocation
       3）当所有的拦截器执行完毕，将旧的Invocation设置到ThreadLocal中，保证外层的拦截器能获取到自己的Invocation

     */


    public static void main(String[] args) {
        String configLoactions = "application.properties";
        GPApplicationContext context = new GPAbstractApplicationContext(configLoactions);
        IDemoService demoService = context.getBean(DemoService.class);
        System.out.println(demoService.get("demoService"));

//        TwoAction twoAction = context.getBean(TwoAction.class);
//        System.out.println(twoAction.getName("TwoAction"));

        UserService userService = context.getBean(UserService.class);
        System.out.println(userService.getName("UserService"));
    }


    @Test
    public void test() throws NoSuchMethodException {
        LogAspect logAspect = new LogAspect();
        Method aspectJAdviceMethod = logAspect.getClass().getMethod("before", GPMethodInvocation.class);
        Type[] paramTypes = aspectJAdviceMethod.getParameterTypes();

        Object[] args = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            System.out.println(paramTypes[i].getTypeName());
            if (paramTypes[i] instanceof GPJoinPoint) {
                System.out.println("instanceof GPJoinPoint");
            } else if (paramTypes[i] instanceof GPMethodInvocation) {
                System.out.println("instanceof GPMethodInvocation");
            } else if (paramTypes[i] instanceof Throwable) {
                System.out.println("instanceof Throwable");
            } else if (paramTypes[i] instanceof Object) {
                System.out.println("instanceof Object");
            }
        }

        Class[] paramTypes1 = aspectJAdviceMethod.getParameterTypes();
        for (int i = 0; i < paramTypes1.length; i++) {
            System.out.println(paramTypes1[i].getTypeName());
            if (paramTypes1[i] == GPJoinPoint.class) {
                System.out.println("instanceof GPJoinPoint");
            } else if (paramTypes1[i] == GPMethodInvocation.class) {
                System.out.println("instanceof GPMethodInvocation");
            } else if (paramTypes1[i] == Throwable.class) {
                System.out.println("instanceof Throwable");
            } else if (paramTypes1[i] == Object.class) {
                System.out.println("instanceof Object");
            }
        }
    }
}

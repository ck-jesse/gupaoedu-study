package com.coy.gupaoedu.study.spring;

import com.coy.gupaoedu.study.spring.demo.aspect.LogAspect;
import com.coy.gupaoedu.study.spring.demo.mvc.action.TwoAction;
import com.coy.gupaoedu.study.spring.demo.service.IDemoService;
import com.coy.gupaoedu.study.spring.demo.service.impl.DemoService;
import com.coy.gupaoedu.study.spring.demo.service.impl.UserService;
import com.coy.gupaoedu.study.spring.framework.aop.aopalliance.intercept.GPJoinpoint;
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
       答：

     */


    public static void main(String[] args) {
        String configLoactions = "application.properties";
        GPApplicationContext context = new GPAbstractApplicationContext(configLoactions);
        IDemoService demoService = context.getBean(DemoService.class);
        System.out.println(demoService.get("demoService"));

        TwoAction twoAction = context.getBean(TwoAction.class);
        System.out.println(twoAction.getName("TwoAction"));

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
            if (paramTypes[i] instanceof GPJoinpoint) {
                System.out.println("instanceof GPJoinpoint");
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
            if (paramTypes1[i] == GPJoinpoint.class) {
                System.out.println("instanceof GPJoinpoint");
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

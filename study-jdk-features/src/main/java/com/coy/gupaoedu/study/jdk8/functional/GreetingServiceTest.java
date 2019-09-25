package com.coy.gupaoedu.study.jdk8.functional;

/**
 *
 * @author chenck
 * @date 2019/9/24 10:32
 */
public class GreetingServiceTest {

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 使用匿名类
        GreetingService greetingService = new GreetingService() {
            @Override
            public void sayMessage(String message) {
                System.out.println(message);
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };
        greetingService.sayMessage("invoke sayMessage using Annonymous class");

        // 使用Lambda表达式
        GreetingService greetingService2 = (message) -> System.out.println(message);
        greetingService2.sayMessage("invoke sayMessage using Lambda expression");
    }


}

package com.coy.gupaoedu.study.spring.demo.factorybean;

/**
 * 自定义FactoryBean创建的bean的具体类型
 *
 * @author chenck
 * @date 2019/6/3 20:48
 */
public class FactoryBeanServiceImpl implements FactoryBeanService {
    @Override
    public String test(String msg) {
        System.out.println(msg);
        return msg;
    }
}

package com.coy.gupaoedu.study.sentinel.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.Map;

/**
 * @Qulifier 的用法
 *
 * @author chenck
 * @date 2019/9/18 23:16
 */
@EnableAutoConfiguration
public class QulifierDemoTest {

    @Autowired
    private Map<String, String> allStringBeans = Collections.emptyMap();

    @Bean
    public String a(){
        return "string-a";
    }

    @Bean
    public String b(){
        return "string-b";
    }

    @Bean
    public String c(){
        return "string-c";
    }

    @Bean
    public void testRun(){
        System.out.println("allStringBeans: "+allStringBeans);
    }


    public static void main(String[] args) {
        SpringApplication.run(QulifierDemoTest.class, args);
    }

}

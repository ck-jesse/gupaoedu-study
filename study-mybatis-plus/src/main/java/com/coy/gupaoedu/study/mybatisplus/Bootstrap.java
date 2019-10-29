package com.coy.gupaoedu.study.mybatisplus;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.coy.gupaoedu.study.mybatisplus.dao.MyBatisTraceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)// 集成排除原生Druid的快速配置类
@Configuration
@MapperScan("com.coy.gupaoedu.study.mybatisplus.dao")
@RestController
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

    /**
     * 定义该方法的目的是为了方便进行http访问测试
     */
    @RequestMapping(value = "/")
    public String index() {
        return "Hi, This is study mybatis! </br>";
    }

    /**
     * 逻辑sql注入器(可实现逻辑删除)
     * 注：3.1.1开始不再需要这一步
     */
    @Bean
    public ISqlInjector sqlInjector() {
        return new LogicSqlInjector();
    }

    @Bean
    @Profile({"dev", "test"})
    public MyBatisTraceInterceptor myBatisTraceInterceptor() {
        return new MyBatisTraceInterceptor();
    }
}
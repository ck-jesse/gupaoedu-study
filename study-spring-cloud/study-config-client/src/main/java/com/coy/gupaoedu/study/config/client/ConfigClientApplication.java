package com.coy.gupaoedu.study.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配置中心 客户端，从配置中心获取配置
 *
 * @author chenck
 * @date 2019/9/4 10:56
 */
@SpringBootApplication
@RestController
public class ConfigClientApplication {

    /**
     * http://localhost:8881/hi
     */
    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class, args);
    }

    /**
     * 从配置中心动态获取配置
     * <p>
     * 实际为从config-client-dev.properties文件中获取foo属性的值
     */
    @Value("${foo}")
    String foo;

    @RequestMapping(value = "/hi")
    public String hi() {
        return foo;
    }

}

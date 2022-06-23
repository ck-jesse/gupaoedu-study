package com.coy.gupaoedu.study.spring;

import com.coy.gupaoedu.study.spring.common.ApplicationContextUtil;
import com.coy.gupaoedu.study.spring.loadfile.ResourceLoadDemo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SpringBootApplication
@RestController
@EnableCaching
public class Application2 {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application2.class, args);

        //
        ResourceLoadDemo resourceLoadDemo = ApplicationContextUtil.getBean(ResourceLoadDemo.class);
        resourceLoadDemo.getResource1("redisson.yaml");
        resourceLoadDemo.getResource2("redisson.yaml");
        resourceLoadDemo.getResource3("redisson.yaml");
        resourceLoadDemo.getResource4("redisson.yaml");
    }

}

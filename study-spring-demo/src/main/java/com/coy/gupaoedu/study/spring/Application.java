package com.coy.gupaoedu.study.spring;

import com.coy.gupaoedu.study.spring.circularreference.ServiceA;
import com.coy.gupaoedu.study.spring.circularreference.ServiceB;
import com.hs.platform.log.access.config.EnableLogAccess;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
//@EnableCaching
@EnableLogAccess
@Import({ServiceA.class, ServiceB.class,})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

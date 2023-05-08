package com.coy.gupaoedu.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author chenck
 * @date 2023/5/8 14:42
 */
@SpringBootApplication
public class MetricsApplication {

    /**
     * http://localhost:29999/actuator/prometheus
     */
    public static void main(String[] args) {
        SpringApplication.run(MetricsApplication.class, args);

    }


}

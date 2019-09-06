package com.coy.gupaoedu.study.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenck
 * @date 2019/9/6 13:24
 */
@SpringBootApplication
@EnableApolloConfig
@RestController
public class ApolloApplication {

    /**
     * http://localhost:8080/getConfig?key=user.id
     */
    public static void main(String[] args) {
        SpringApplication.run(ApolloApplication.class, args);
    }

    @ApolloConfig
    private Config config;

    @RequestMapping(value = "getConfig", method = {RequestMethod.GET, RequestMethod.POST})
    public String getConfig(String key) {
        String value = config.getProperty(key, "defaultValue");
        System.out.println(value);
        return value;
    }
}

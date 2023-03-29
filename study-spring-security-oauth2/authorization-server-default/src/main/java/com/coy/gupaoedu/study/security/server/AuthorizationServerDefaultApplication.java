package com.coy.gupaoedu.study.security.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HttpSecurity 是基于Builder模式来实现的。
 * <p>
 * 官方文档： https://docs.spring.io/spring-security/site/docs/5.1.6.RELEASE/reference/htmlsingle/#jc-hello-wsca
 */
@SpringBootApplication
@RestController
public class AuthorizationServerDefaultApplication {

    /**
     * http://localhost:8080/hello?name=123
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerDefaultApplication.class, args);
    }

    @GetMapping("/hello")
    public Person hello(String name) {
        Person person = new Person(name, 18);
        return person;
    }
}

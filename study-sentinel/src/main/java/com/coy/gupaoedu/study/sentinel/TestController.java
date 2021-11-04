package com.coy.gupaoedu.study.sentinel;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @Autowired
    private TestService service;

    @GetMapping(value = "/hello/{name}")
    public String apiHello(@PathVariable String name) {
        return service.sayHello(name);
    }

    @GetMapping("hello")
    @SentinelResource(value = "test.hello", fallback = "helloError")
    public String hello(String name) {
        return "hello," + name;
    }

    public String helloError(String name, Throwable e) {
        return "error," + name;
    }

}


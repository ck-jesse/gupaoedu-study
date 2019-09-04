package com.coy.gupaoedu.study.eureka.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author chenck
 * @date 2019/9/4 11:37
 */
@RestController
public class UserController {

    @Value("${server.port}")
    String port;

    @RequestMapping("/home")
    public String home(@RequestParam(value = "name", defaultValue = "provider") String name) {
        return "provider-service: hi " + name + " ,i am from port:" + port;
    }

}

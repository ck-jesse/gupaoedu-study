package com.coy.gupaoedu.study.eureka.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenck
 * @date 2019/9/4 11:37
 */
@RestController
public class UserController {

    @Autowired
    UserServiceFeignApi userServiceFeignApi;

    @RequestMapping("/home")
    public String home(@RequestParam(value = "name", defaultValue = "consumer") String name) {
        return "consumer-service: " + userServiceFeignApi.home(name);
    }

}

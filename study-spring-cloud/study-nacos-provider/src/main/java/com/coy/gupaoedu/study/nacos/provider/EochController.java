package com.coy.gupaoedu.study.nacos.provider;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenck
 * @date 2019/9/4 11:37
 */
@RestController
public class EochController {

    @RequestMapping("/echo")
    public String echo(String name) {
        return "nocas-provider-service: Hello Nacos Discovery  " + name;
    }

}

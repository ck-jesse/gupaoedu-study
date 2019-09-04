package com.coy.gupaoedu.study.nacos.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenck
 * @date 2019/9/4 11:37
 */
@RestController
public class EchoController {

    @Autowired
    EchoServiceFeignApi echoServiceFeignApi;

    @RequestMapping("/echo")
    public String echo(String name) {
        return "nacos-consumer-service: " + echoServiceFeignApi.echo(name);
    }

}

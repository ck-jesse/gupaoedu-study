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

    /**
     * feign方式调用服务，可被加入到zipkin追踪链路中
     */
    @Autowired
    EchoServiceFeignApi echoServiceFeignApi;

    @RequestMapping("/echo")
    public String echo(String name) {
        return "fegin + ribbon 方式：</br>nacos-consumer-service: " + echoServiceFeignApi.echo(name);
    }

}

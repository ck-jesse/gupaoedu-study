package com.coy.gupaoedu.study.nacos.consumer;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author chenck
 * @date 2019/9/4 11:37
 */
@RestController
public class RibbonTestController {

    private final RestTemplate restTemplate;

    @Autowired
    public RibbonTestController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 在ribbon使用断路器
    // 添加Hystrix熔断器
    @HystrixCommand(fallbackMethod = "error")
    @RequestMapping(value = "/ribbon/echo", method = RequestMethod.GET)
    public String echo(String name) {
        return "ribbon + restTemplate方式: </br>" + restTemplate.getForObject("http://nacos-provider-service/echo?name" +
                "=" + name, String.class);
    }

    public String error(String name) {
        return "hi," + name + ",sorry,error! 触发Hystrix熔断器";
    }
}

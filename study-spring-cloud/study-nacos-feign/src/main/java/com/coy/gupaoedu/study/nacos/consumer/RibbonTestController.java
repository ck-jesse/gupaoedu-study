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

    /**
     * TODO ribbon+restTemplate实现服务调用方式，不会被拦截并加入到zipkin追踪链路中去，这是为啥？
     * 分析：是可以的，zipkin-ui有点坑，不方便看，排序也是乱的
     */
    // 在ribbon使用断路器
    // 添加Hystrix熔断器
    @HystrixCommand(fallbackMethod = "error")
    @RequestMapping(value = "/ribbon/echo", method = RequestMethod.GET)
    public String echo(String name) {
        return "ribbon + restTemplate方式: </br>" + restTemplate.getForObject("http://nacos-provider-service/echo?name" +
                "=" + name, String.class);
    }

    public String error(String name) {
        String msg = "hi," + name + ",sorry,error! 触发Hystrix熔断器";
        System.out.println(msg);
        return msg;
    }
}

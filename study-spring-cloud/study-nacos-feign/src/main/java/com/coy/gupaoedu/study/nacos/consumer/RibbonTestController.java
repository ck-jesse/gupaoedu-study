package com.coy.gupaoedu.study.nacos.consumer;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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
    * 在ribbon使用断路器
    * 添加Hystrix熔断器
     * TODO ribbon+restTemplate实现服务调用方式，不会被拦截并加入到zipkin追踪链路中去，这是为啥？
     * 分析：是可以的，zipkin-ui有点坑，不方便看，排序也是乱的
     *
     * hystrix 配置文档： https://github.com/Netflix/Hystrix/wiki/Configuration
     *
     *  @HystrixCommand 是基于 AspectJ 来实现的，可借鉴其注解中定义的 fallbackMethod 异常处理的方法的处理
     */
    @HystrixCommand(fallbackMethod = "error",commandProperties = {
            // 基于thread可实现超时熔断，优点：超时时间可控制最大可用时间，缺点：存在线程切换，会导致ThreadLocal/Spring事务/Cache等出现问题
            // @HystrixProperty(name="execution.isolation.strategy",value = "THREAD")
            // 基于semaphore信号量实现熔断，优点：，缺点：存在堵塞，超过最大信号量则会堵塞
            @HystrixProperty(name="execution.isolation.strategy",value = "SEMAPHORE")
    })
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

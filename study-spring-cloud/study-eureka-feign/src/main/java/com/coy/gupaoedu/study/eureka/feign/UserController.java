package com.coy.gupaoedu.study.eureka.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenck
 * @date 2019/9/4 11:37
 */
@RestController
public class UserController {

    @Autowired
    UserServiceFeignApi userServiceFeignApi;

    // 组合实现，支持多种注册中心 eureka consul zookeeper nacos
    @Autowired
    DiscoveryClient discoveryClient;

    @RequestMapping("/home")
    public String home(@RequestParam(value = "name", defaultValue = "consumer") String name) {
        return "consumer-service: " + userServiceFeignApi.home(name);
    }

    @RequestMapping(value = "/getServices", method = {RequestMethod.GET, RequestMethod.POST})
    public List<String> getServices() {
        return discoveryClient.getServices();
    }

    @RequestMapping(value = "/getServiceInstances", method = {RequestMethod.GET, RequestMethod.POST})
    public List<ServiceInstance> getServiceInstances(String serviceName) {
        return discoveryClient.getInstances(serviceName);
    }

}

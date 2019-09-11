package com.coy.gupaoedu.study.zipkin.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * zipkin 服务链路追踪
 *
 * @author chenck
 * @date 2019/9/4 13:05
 */
@SpringBootApplication
@RestController
@EnableDiscoveryClient// 开启服务注册发现功能
public class ZipkinClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinClientApplication.class, args);
    }

    private static final Logger LOG = Logger.getLogger(ZipkinClientApplication.class.getName());

    @Autowired
    private RestTemplate restTemplate;

    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /**
     * 为啥这里不能出现在链路追踪zipkin中呢？
     * 是因为 nacos-provider-service 采用了nacos注册中心，而study-zipkin-client没有启动nacos注册中心所以请求不能被zipkin给拦截到吗？
     *
     * zipkin 的链路追踪日志不是很可控（不能实施收集到），需要分析其原理，来更好的理解它是怎么实现链路追踪的
     */
    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    public String callHome(String name) {
        LOG.log(Level.INFO, "calling trace nacos-provider-service  ");
        //return restTemplate.getForObject("http://localhost:8763/echo", String.class);
        return "zipkin 追踪: </br>" + restTemplate.getForObject("http://nacos-provider-service/echo?name" +
                "=" + name, String.class);
    }


    /**
     * 服务仅仅内部调用可出现追踪
     */
    @RequestMapping("/info")
    public String info() {
        LOG.log(Level.INFO, "calling trace service-hi ");
        return "i'm service-hi";
    }

}

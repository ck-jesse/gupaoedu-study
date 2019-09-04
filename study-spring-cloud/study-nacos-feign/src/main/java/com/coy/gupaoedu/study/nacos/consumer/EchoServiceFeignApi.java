package com.coy.gupaoedu.study.nacos.consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 定义一个Feign接口
 * <p>
 * 原理：基于动态代理生成一个调用服务方接口http的代理对象
 *
 * @author chenck
 * @date 2019/9/4 11:58
 */
// Feign中使用断路器
// Feign是自带断路器的，在D版本的Spring Cloud之后，它没有默认打开。需要在配置文件中配置打开它，
// feign.hystrix.enabled=true
@FeignClient(value = "nacos-provider-service", fallback = EchoServiceFeignApiImpl.class)// 指定服务提供方的名字
public interface EchoServiceFeignApi {

    @RequestMapping(value = "/echo", method = RequestMethod.GET)
    String echo(@RequestParam(value = "name") String name);
}

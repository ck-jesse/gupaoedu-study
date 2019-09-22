package com.coy.gupaoedu.study.eureka.feign;

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
@FeignClient(value = "eureka-provider-service")// 指定服务提供方的名字
public interface UserServiceFeignApi {

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    String home(@RequestParam(value = "name") String name);
}

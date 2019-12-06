package com.coy.gupaoedu.study.eureka.feign;

import feign.Request;
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

    /**
     * 增加一个方法入参 Request.Options options，用于设置单个方法的连接超时时间、读取超时时间
     * 注：通过分析源码，得知可通过该方式来设置单个方法的超时时间：
     * feign.SynchronousMethodHandler#invoke(Object[] argv) => feign代理对象的执行入口
     * feign.SynchronousMethodHandler#findOptions(argv) => 关键点在这里
     * findOptions(argv) 方法会判断方法入参数组对象中是否包含了Request.Options类型的对象，如果有，则取出，如果没有，则取默认的Request.Options
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    String home(@RequestParam(value = "name") String name, Request.Options options);
}

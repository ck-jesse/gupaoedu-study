package com.coy.gupaoedu.study.nacos.consumer;

import org.springframework.stereotype.Component;

/**
 * Feign中使用断路器
 * 需要实现EchoServiceFeignApi 接口，并注入到Ioc容器中
 *
 * @author chenck
 * @date 2019/9/4 15:40
 */
@Component
public class EchoServiceFeignApiImpl implements EchoServiceFeignApi {
    @Override
    public String echo(String name) {
        return "sorry," + name + " 触发feign的自带断路器";
    }
}

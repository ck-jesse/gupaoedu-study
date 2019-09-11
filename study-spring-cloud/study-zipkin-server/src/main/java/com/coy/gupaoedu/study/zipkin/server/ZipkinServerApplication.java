package com.coy.gupaoedu.study.zipkin.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin2.server.internal.EnableZipkinServer;

/**
 * zipkin 服务链路追踪
 *
 * @author chenck
 * @date 2019/9/4 13:05
 */
@SpringBootApplication
@EnableZipkinServer // 开启ZipkinServer功能
public class ZipkinServerApplication {

    /**
     * http://localhost:9001
     *
     * 注意：
     * 请结合 study-zipkin-client / study-nacos-provider / study-nacos-feign 来验证服务链路追踪
     *
     * TODO zipkin 的链路追踪日志不是很可控，需要分析其原理，来更好的理解它是怎么实现链路追踪的
     * 1、不能实施收集到日志吗？猜想是异步推送追踪日志到zipkin服务端的，如果是那么异步推送间隔是多长？
     *
     */
    public static void main(String[] args) {
        SpringApplication.run(ZipkinServerApplication.class, args);
    }
}

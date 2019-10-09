package com.coy.gupaoedu.study.spring.cloud.gateway.custom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

    /**
     * 自定义过滤器工厂类
     * @return
     */
    @Bean
    public RequestTimeGatewayFilterFactory requestTimeGatewayFilterFactory(){
        return new RequestTimeGatewayFilterFactory();
    }

    /**
     * 自定义全局过滤器 - token 全局过滤器
     * @return
     */
    /*@Bean
    public TokenGlobalFilter tokenGlobalFilter(){
        return new TokenGlobalFilter();
    }*/

    /**
     * 自定义限流的键的解析器
     *
     * TODO 为什么只能有一个 自定义KeyResolver？HostAddrKeyResolver 和 UriKeyResolver 不能同时存在？
     * @return
     */
    @Bean
    public HostAddrKeyResolver hostAddrKeyResolver(){
        return new HostAddrKeyResolver();
    }

    /**
     * 自定义限流的键的解析器
     * @return
     */
     /*@Bean
    public UriKeyResolver uriKeyResolver(){
        return new UriKeyResolver();
    }*/
}

package com.coy.gupaoedu.study.spring.cloud.gateway.custom;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Spring Cloud Gateway 第一个 demo
 */
@Configuration
public class GatewayRouteLocatorConfiguration {

    /**
     * RouteLocator 路由定位器
     *
     * 使用RouteLocator的Bean进行路由转发，将请求进行处理，最后转发到目标的下游服务。
     * http://localhost:8080/get
     * <p>
     * 通过postman访问 http://localhost:8080/delay/3 ，并在Header中设置 Host=www.hystrix.com，会走到 fallback逻辑
     */
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        // 作为转发请求的域名部分
        // http://httpbin.org:80/get
        // http://httpbin.org:80/ip
        String httpBinUrl = "http://httpbin.org:80";

        // 通过 RouteLocatorBuilder 创建路由，同时可添加各种 predicates和filters
        // predicates断言的意思，顾名思义就是根据具体的请求规则，去匹配具体的route去处理，
        // filters是各种过滤器，用来对请求做各种判断和修改。
        return builder.routes()
                // 定义一个 router（函数式编程）
                // 此处定义了一个 predicate 断言配置，使请求与该 router 匹配，该router配置实现功能：让“/get”请求都转发到“http://httpbin.org/get”
                .route(p -> p
                        .path("/get")
                        // 添加一个 filter ，往请求头里面添加一个Header(key=Hello,value=World)
                        .filters(f -> f.filter(new RequestTimeGatewayFilter()).addRequestHeader("Hello", "World"))
                        // 路由的目标url，实际为重定向地址
                        .uri(httpBinUrl))

                // 定义一个 router（普通编程） 用于与上面比较
                .route(new Function<PredicateSpec, Route.AsyncBuilder>(){
                    @Override
                    public Route.AsyncBuilder apply(PredicateSpec predicateSpec) {
                        return predicateSpec.path("/ip")
                                .filters(new Function<GatewayFilterSpec, UriSpec>(){
                                    @Override
                                    public UriSpec apply(GatewayFilterSpec gatewayFilterSpec) {
                                        // 自定义filter过滤器
                                        return gatewayFilterSpec.filter(new RequestTimeGatewayFilter()).addRequestHeader("Hello", "World");
                                    }
                                })
                                .uri(httpBinUrl);
                    }
                })

                // 再定义一个 router
                // 使用hystrix 服务熔断降级，当请求头中的Host匹配"*.hystrix.com"时，会进入该 router
                .route(p -> p
                        .host("*.hystrix.com")
                        .filters(f -> f.hystrix(config -> config
                                .setName("mycmd")// 设置资源名字
                                .setFallbackUri("forward:/fallback"))// 失败地址
                        )
                        .uri(httpBinUrl))
                .build();
    }

    /**
     * Mono是一个Reactive stream
     */
    @RequestMapping("/fallback")
    public Mono<String> fallback() {
        return Mono.just("fallback");
    }

}

package com.coy.gupaoedu.study.spring.cloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class SpringCloudGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGatewayApplication.class, args);
    }

    /**
     * 使用RouteLocator的Bean进行路由转发，将请求进行处理，最后转发到目标的下游服务。
     * http://localhost:8080/get
     * <p>
     * 通过postman访问 http://localhost:8080/delay/3 ，并在Header中设置 Host=www.hystrix.com，会走到 fallback逻辑
     */
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/get")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri("http://httpbin.org:80"))
                // 使用hystrix
                .route(p -> p
                        .host("*.hystrix.com")
                        .filters(f -> f.hystrix(config -> config
                                .setName("mycmd")// 设置资源名字
                                .setFallbackUri("forward:/fallback"))// 失败地址
                        )
                        .uri("http://httpbin.org:80"))
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

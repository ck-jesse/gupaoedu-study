package com.coy.gupaoedu.study.spring.cloud.gateway.custom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义过滤器 GatewayFilter ，打印请求时间
 *
 * 注意：需要将该自定义过滤器注册到网关Route中才能生效，所以需要结合GatewayRouteLocatorConfiguration 中定义的 RouteLocatorBuilder 来创建路由，和添加各种 predicates和filters。
 */
public class RequestTimeGatewayFilter implements GatewayFilter, Ordered {

    private static final Log log = LogFactory.getLog(RequestTimeGatewayFilter.class);
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 先记录了请求的开始时间，并保存在ServerWebExchange中，此处是一个“pre”类型的过滤器
        exchange.getAttributes().put("REQUEST_TIME_BEGIN", System.currentTimeMillis());
        // 然后再chain.filter的内部类中的run()方法中相当于”post”过滤器，在此处打印了请求所消耗的时间。
        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    Long startTime = exchange.getAttribute("REQUEST_TIME_BEGIN");
                    if (startTime != null) {
                        System.out.println("自定义filter过滤器："+exchange.getRequest().getURI().getRawPath() + ": " + (System.currentTimeMillis() - startTime) + "ms");
                        log.info("自定义filter过滤器："+exchange.getRequest().getURI().getRawPath() + ": " + (System.currentTimeMillis() - startTime) + "ms");
                    }
                })
        );
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

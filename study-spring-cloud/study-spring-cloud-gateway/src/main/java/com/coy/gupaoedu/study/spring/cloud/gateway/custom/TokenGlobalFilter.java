package com.coy.gupaoedu.study.spring.cloud.gateway.custom;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义全局过滤器 - GlobalFilter - token 过滤器
 */
//@Component
public class TokenGlobalFilter implements GlobalFilter, Ordered {

    private static final Log log = LogFactory.getLog(TokenGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if(StringUtils.isBlank(token)){
            log.info(exchange.getRequest().getURI().getRawPath() + " token is empty");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        // 继续执行其他filter
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}

package com.coy.gupaoedu.study.spring.cloud.gateway.custom;

import com.coy.gupaoedu.study.spring.cloud.gateway.util.MDCLogTracerUtil;
import com.coy.gupaoedu.study.spring.cloud.gateway.util.TracingVariable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 自定义trace_id全局过滤器
 *
 * @author chenck
 * @date 2023/4/10 11:34
 */
@Component
@Slf4j
public class TraceIdFilter implements GlobalFilter, Ordered {

    public static final String REQUEST_ID = "X-Request-ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String traceId = headers.getFirst(TracingVariable.TRACE_ID);

        // 请求头中未传入trace_id，则默认生成一个trace_id
        if (StringUtils.isEmpty(traceId)) {
            traceId = genTraceId();
            MDCLogTracerUtil.attachTraceId(traceId);

            // 设置trace_id到请求头中，透传到下游服务中
            ServerHttpRequest mutateRequest = exchange.getRequest().mutate().header(TracingVariable.TRACE_ID, traceId).build();
            exchange = exchange.mutate().request(mutateRequest).build();
            if (log.isDebugEnabled()) {
                log.debug("请求头中未传入trace_id，则默认生成一个 {}", traceId);
            }
        } else {
            MDCLogTracerUtil.attachTraceId(traceId);
        }

        return chain.filter(exchange).doFinally(signalType -> {
            MDCLogTracerUtil.removeTraceId();
        });
    }

    @Override
    public int getOrder() {
        return -100;
    }

    /**
     * 生成trace_id
     */
    private String genTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}

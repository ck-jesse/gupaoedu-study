package com.coy.gupaoedu.study.spring.cloud.gateway.custom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 自定义过滤器工厂类
 * 注意：可以在配置文件中配置过滤器
 *
 * 测试地址： http://localhost:8081/?name=yes 会进入到该过滤器工厂
 */
@Component
public class RequestTimeGatewayFilterFactory extends AbstractGatewayFilterFactory<RequestTimeGatewayFilterFactory.Config> {

    private static final Log log = LogFactory.getLog(RequestTimeGatewayFilterFactory.class);
    private static final String REQUEST_TIME_BEGIN = "requestTimeBegin";
    private static final String KEY = "withParams";

    public RequestTimeGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(KEY);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // 创建一个GatewayFilter的匿名类
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                // 先记录了请求的开始时间，并保存在ServerWebExchange中，此处是一个“pre”类型的过滤器
                exchange.getAttributes().put(REQUEST_TIME_BEGIN, System.currentTimeMillis());
                // 然后再chain.filter的内部类中的run()方法中相当于”post”过滤器，在此处打印了请求所消耗的时间。
                return chain.filter(exchange).then(
                        Mono.fromRunnable(() -> {
                            Long startTime = exchange.getAttribute(REQUEST_TIME_BEGIN);
                            if (startTime != null) {
                                StringBuilder sb = new StringBuilder(exchange.getRequest().getURI().getRawPath())
                                        .append(": ")
                                        .append(System.currentTimeMillis() - startTime)
                                        .append("ms");
                                if (config.isWithParams()) {
                                    sb.append(" params:").append(exchange.getRequest().getQueryParams());
                                }
                                log.info("自定义filter过滤器："+sb.toString());
                            }
                        })
                );
            }
        };
    }

    public static class Config {

        private boolean withParams;

        public boolean isWithParams() {
            return withParams;
        }

        public void setWithParams(boolean withParams) {
            this.withParams = withParams;
        }

    }
}

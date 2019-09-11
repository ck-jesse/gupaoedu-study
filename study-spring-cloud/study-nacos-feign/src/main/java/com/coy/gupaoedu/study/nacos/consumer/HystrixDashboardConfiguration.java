package com.coy.gupaoedu.study.nacos.consumer;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Hystrix Dashboard是作为断路器状态的一个组件，提供了数据监控和友好的图形化界面。
 *
 * @author chenck
 * @date 2019/9/11 15:52
 */
@Configuration
@EnableHystrixDashboard // 开启Hystrix熔断器Dashboard功能
public class HystrixDashboardConfiguration {

    /**
     * hystrix-dashboard 监控面板
     * 单个服务的 dashboard : http://localhost:8764/actuator/hystrix.stream
     */
    @Bean
    public ServletRegistrationBean getServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;

    }
}

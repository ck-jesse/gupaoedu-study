package com.coy.gupaoedu.study.spring.cloud.gateway.util;

/**
 * @author chenck
 * @date 2020/9/1 16:55
 */
public class TracingVariable {

    public static final String TRACE_ID = "sid";// 分布式应用链路追踪id
    /**
     * 调用层级id，可通过 opentracing 实现SPAN。
     * SPAN 表示分布式调用链条中的一个调用单元
     */
    public static final String SPAN_ID = "span_id";// 调用层级id
    public static final String LOCAL_IP = "local_ip";
}

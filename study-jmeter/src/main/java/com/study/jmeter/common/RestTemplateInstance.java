package com.study.jmeter.common;

import com.googlecode.protobuf.format.JsonFormat;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * @author chenck
 * @date 2019/11/21 14:45
 */
public class RestTemplateInstance {

    /**
     * protobuf 实体文件格式化
     */
    public static final JsonFormat jsonFormat = new JsonFormat();

    /**
     * 定义针对protobuf序列化的RestTemplate
     */
    public static final RestTemplate protoRestTemplate = new RestTemplate(Collections.singletonList(new ProtobufHttpMessageConverter()));

}

package com.study.jmeter.common;

import com.googlecode.protobuf.format.JsonFormat;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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
    //public static final RestTemplate protoRestTemplate = new RestTemplate(Collections.singletonList(new ProtobufHttpMessageConverter()));
    public static final RestTemplate protoRestTemplate;

    static {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new ProtobufHttpMessageConverter());
        protoRestTemplate = new RestTemplate(messageConverters);
    }
}

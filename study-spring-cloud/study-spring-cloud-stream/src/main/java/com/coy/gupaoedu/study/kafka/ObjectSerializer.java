package com.coy.gupaoedu.study.kafka;

import org.apache.kafka.common.serialization.Serializer;

import java.io.Serializable;
import java.util.Map;

/**
 * 序列化
 *
 * @author chenck
 * @date 2019/9/22 22:54
 */
public class ObjectSerializer implements Serializer<Serializable> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, Serializable serializable) {
        return new byte[0];
    }

    @Override
    public void close() {

    }
}

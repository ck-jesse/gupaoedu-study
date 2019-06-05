package com.coy.gupaoedu.study.serialization.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 基于Fastjson的序列化
 *
 * @author chenck
 * @date 2019/6/3 11:49
 */
public class FastJsonSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj, Class<T> clazz) {
        JSONSerializer serializer = new JSONSerializer();
        serializer.config(SerializerFeature.WriteEnumUsingToString, true);
        serializer.write(obj);
//        return serializer.toString().getBytes();
        return JSON.toJSONString(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] objByteArr, Class<T> clazz) {
        return JSON.parseObject(objByteArr, clazz);
    }
}

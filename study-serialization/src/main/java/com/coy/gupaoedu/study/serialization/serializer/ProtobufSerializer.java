package com.coy.gupaoedu.study.serialization.serializer;

import com.coy.gupaoedu.study.serialization.proto.ProtostuffUtil;

/**
 * 基于Protobuf的序列化
 * <p>
 * 注意：protobuf 协议针对复杂对象序列化后的大小，可能比fastjson序列化后的大小还要大，所以protobuf序列化建议针对结构简单的对象（如只有一层的对象）
 *
 * @author chenck
 * @date 2019/6/3 11:49
 */
public class ProtobufSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj, Class<T> clazz) {
        return ProtostuffUtil.serializer(obj);
    }

    @Override
    public <T> T deserialize(byte[] objByteArr, Class<T> clazz) {
        return ProtostuffUtil.deserializer(objByteArr, clazz);
    }
}

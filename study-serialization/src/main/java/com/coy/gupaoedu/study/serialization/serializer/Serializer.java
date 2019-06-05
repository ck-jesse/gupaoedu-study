package com.coy.gupaoedu.study.serialization.serializer;

import java.io.IOException;

/**
 * 序列化
 *
 * @author chenck
 * @date 2019/6/3 11:26
 */
public interface Serializer {

    /**
     * 序列化
     *
     * @param obj
     * @param clazz
     * @return byte[]
     * @author chenck
     * @date 2019/6/3 11:28
     */
    public <T> byte[] serialize(T obj, Class<T> clazz) throws IOException;

    /**
     * 反序列化
     *
     * @param objByteArr
     * @param clazz
     * @return T
     * @author chenck
     * @date 2019/6/3 11:30
     */
    public <T> T deserialize(byte[] objByteArr, Class<T> clazz) throws IOException, ClassNotFoundException;
}

package com.coy.gupaoedu.study.serialization.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Java原生序列化
 *
 * @author chenck
 * @date 2019/6/3 11:49
 */
public class JavaSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj, Class<T> clazz) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(obj);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] objByteArr, Class<T> clazz) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objByteArr);
        ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
        return (T) inputStream.readObject();
    }
}

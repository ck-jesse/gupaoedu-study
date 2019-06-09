package com.coy.gupaoedu.study.serialization.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Java原生序列化
 * 注意：被序列化的对象需要实现java.io.Serializable接口
 * 缺点：序列化的数据比较大，传输效率低
 *
 * @author chenck
 * @date 2019/6/3 11:49
 */
public class JavaSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj, Class<T> clazz) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 对象输出流
        ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        // 将obj对象进行序列化，并把得到的字节序列写到一个目标输出流中
        outputStream.writeObject(obj);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] objByteArr, Class<T> clazz) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objByteArr);
        // 对象输入流
        ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
        // 从源输入流中读取字节序列，并反序列化为一个对象
        return (T) inputStream.readObject();
    }
}

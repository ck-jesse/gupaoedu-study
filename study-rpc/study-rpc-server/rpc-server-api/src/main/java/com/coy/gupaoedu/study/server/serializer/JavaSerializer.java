package com.coy.gupaoedu.study.server.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Java原生序列化
 * 注意：被序列化的对象需要实现java.io.Serializable接口
 * 缺点：序列化的数据比较大，传输效率低
 * <p>
 * 序列化是把对象改成可以存到磁盘或通过网络发送到其他运行中的 Java 虚拟机的二进制格式的过程, 并可以通过反序列化恢复对象状态.
 * Java 序列化API给开发人员提供了一个标准机制, 通过 java.io.Serializable 和 java.io.Externalizable 接口, ObjectInputStream 及ObjectOutputStream 处理对象序列化.
 * <p>
 * Serializable接口它没有任何方法，在 Java 中也称为标记接口。当类实现 java.io.Serializable 接口时, 它将在 Java 中变得可序列化, 并指示编译器使用 Java 序列化机制序列化此对象。
 *
 * @author chenck
 * @date 2019/6/3 11:49
 */
public class JavaSerializer implements Serializer {

    /**
     * obj 必须实现Serializable接口，否则报NotSerializableException
     */
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

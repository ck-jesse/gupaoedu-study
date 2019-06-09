package com.coy.gupaoedu.study.serialization.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * hession序列化
 * Hessian是一个支持跨语言传输的二进制序列化协议，相对于 Java 默认的序列化机制来说， Hessian 具有更好的性能和易用性
 *
 * @author chenck
 * @date 2019/6/9 15:23
 */
public class HessionSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T obj, Class<T> clazz) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(outputStream);
        hessianOutput.writeObject(obj);
        return outputStream.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] objByteArr, Class<T> clazz) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objByteArr);
        // 对象输入流
        HessianInput hessianInput = new HessianInput(byteArrayInputStream);
        // 从源输入流中读取字节序列，并反序列化为一个对象
        return (T) hessianInput.readObject();
    }
}

package com.coy.gupaoedu.study.serialization.serializer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.IOException;

/**
 * XML序列化
 *
 * @author chenck
 * @date 2019/6/3 11:49
 */
public class XStreamSerializer implements Serializer {

    XStream xStream = new XStream(new DomDriver());

    @Override
    public <T> byte[] serialize(T obj, Class<T> clazz) throws IOException {
        return xStream.toXML(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] objByteArr, Class<T> clazz) throws IOException, ClassNotFoundException {
        return (T) xStream.fromXML(new String(objByteArr));
    }
}

package com.coy.gupaoedu.study.serialization;

import com.coy.gupaoedu.study.serialization.model.User;
import com.coy.gupaoedu.study.serialization.serializer.FastJsonSerializer;
import com.coy.gupaoedu.study.serialization.serializer.Serializer;

import java.io.IOException;

/**
 * @author chenck
 * @date 2019/6/3 11:46
 */
public class SerializerTest {

        /*
    3、数据序列化与反序列化（按照约定的数据格式进行序列化和反序列化即可）
	不同的序列化后的数据大小不同，越小传输性能越高
	java原生序列化
	hessian
	fastjson
	jackson
	gson
	xml
	protobuf 压缩
	能不能不定义属性？定义格式：TLV, 字符串的格式： Tag|length|value  数字的格式：Tag|value(因为数字没有长度)
	能不能对数字进行压缩？
	实现样例，体现对象转字节流后的大小，以及数据格式也会影响大小
    bitmap 布隆过滤器

    字节的压缩达到的目标
    1、了解压缩的思想（亿级数据的存储和检索、如何在2G内存中存取数据）
    2、巩固基础
     */

    public static void main(String[] args) throws IOException, ClassNotFoundException {

//        Serializer serializer = new JavaSerializer();
        Serializer serializer = new FastJsonSerializer();

        User user = new User();
        user.setName("coy");
        user.setAge(18);

        byte[] bytes = serializer.serialize(user, User.class);
        System.out.println("serialize.length=" + bytes.length);
        System.out.println(new String(bytes));

        User userNew = serializer.deserialize(bytes, User.class);
        System.out.println("name=" + userNew.getName() + ",age=" + userNew.getAge());
    }
}

package com.coy.gupaoedu.study.serialization;

import com.coy.gupaoedu.study.serialization.model.UserProtos;

/**
 * @author chenck
 * @date 2019/6/9 15:30
 */
public class ProtobufTest {

    public static void main(String[] args) {
        UserProtos.User user = UserProtos.User.newBuilder().
                setAge(300).setName("Mic").build();

        byte[] bytes = user.toByteArray();
        System.out.println(bytes.length);

        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + " ");
        }
        // 输出结果：长度为8，内容如下：
        // 10 3 77 105 99 16 18

        // Protobuf序列化后的数据体积小 & 序列化速度快，最终使得传输效率高，

    }
}

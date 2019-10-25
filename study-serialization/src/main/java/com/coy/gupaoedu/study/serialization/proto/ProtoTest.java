package com.coy.gupaoedu.study.serialization.proto;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.LazyStringArrayList;
import com.google.protobuf.LazyStringList;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author chenck
 * @date 2019/10/22 15:34
 */
public class ProtoTest {

    @Test
    public void listTest() {
        LazyStringArrayList list = new LazyStringArrayList();
        list.add("11");
        list.add("22");
        list.add("33");
        byte[] msgProtoObj = ProtostuffUtil.serializer(list);
        System.out.println(msgProtoObj.length);
        System.out.println(new String(msgProtoObj));

        LazyStringArrayList newMsgObj = ProtostuffUtil.deserializer(msgProtoObj,
                LazyStringArrayList.class);
        System.out.println(JSON.toJSONString(newMsgObj));
    }

    /**
     * TODO 针对通过 protoc 命令生成的实体对象进行序列化时，如果实体中包含了List类型的字段(通过repeated生成)，那么在反序列化时会报错
     * java.lang.InstantiationError: com.google.protobuf.LazyStringList
     * 注：本来的想法是，通过protostuff来兼容到普通pojo和proto实体的序列化，但是验证下来发现该问题，此问题暂时还没有找到解决方法，待处理？
     */
    @Test
    public void protocEntityTest() {
        SendMsgRequestProto.GetSendResultResponse.Builder builder = SendMsgRequestProto.GetSendResultResponse.newBuilder();
        builder.setMsg("ok");
        builder.setCode(SendMsgRequestProto.ResponseCode.RESP_CODE_SUCCESS);

        System.setProperty("sun.reflect.noInflation", "false");
        System.setProperty("sun.reflect.inflationThreshold", "15");
        LazyStringArrayList list = new LazyStringArrayList();
        list.add("11");
        list.add("22");
        list.add("33");
        builder.addAllFailedUserIdList(list);// 发送失败的用户id

//        builder.addFailedUserIdList("111");
//        builder.addFailedUserIdList("222");
//        builder.addFailedUserIdList("333");

        byte[] msgProtoObj = ProtostuffUtil.serializer(builder.build());
        System.out.println(msgProtoObj.length);
        System.out.println(new String(msgProtoObj));

        // TODO 针对通过 protoc 命令生成的实体对象进行序列化时，如果实体中包含了List类型的字段(通过repeated生成)，那么在反序列化时会报错：
        // java.lang.InstantiationError: com.google.protobuf.LazyStringList
        // 此问题该怎么解决？
        SendMsgRequestProto.GetSendResultResponse newMsgObj = ProtostuffUtil.deserializer(msgProtoObj,
                SendMsgRequestProto.GetSendResultResponse.class);
        System.out.println(JSON.toJSONString(newMsgObj));
    }

    @Test
    public void objTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = Object.class.getConstructor(null);
        Object obj = constructor.newInstance(null);
        System.out.println(obj);

        Constructor constructor1 = LazyStringArrayList.class.getConstructor(null);
        LazyStringList lazyStringList = (LazyStringList) constructor1.newInstance(null);
        System.out.println(lazyStringList);
    }
}

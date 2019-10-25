package com.coy.gupaoedu.study.serialization.proto;

import com.alibaba.fastjson.JSON;
import com.coy.gupaoedu.study.serialization.proto.msg.ActionCard;
import com.coy.gupaoedu.study.serialization.proto.msg.ActionCard.BtnJsonList;
import com.coy.gupaoedu.study.serialization.proto.msg.SendMsgRequest;
import com.coy.gupaoedu.study.serialization.proto.msg.SendMsgRequest1;
import com.coy.gupaoedu.study.serialization.proto.msg.Text;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

/**
 * https://developers.google.cn/protocol-buffers/docs/proto3
 *
 * @author chenck
 * @date 2019/10/21 10:06
 */
public class ProtobufTest {

    public static void main(String[] args) {
        Person person = new Person();
        person.setUserId(1);
        person.setUserTypeId(1);
        person.setUserName("XRQ");
        person.setCreateDateTime(new Date());
        //序列化成ProtoBuf数据结构
        byte[] userProtoObj = ProtostuffUtil.serializer(person);
        System.out.println(userProtoObj.length);
        System.out.println(new String(userProtoObj));

        //ProtoBuf数据结构反序列化成User对象
        Person newUserObj = ProtostuffUtil.deserializer(userProtoObj, Person.class);
        System.out.println(JSON.toJSONString(newUserObj));

    }

    @Test
    public void test1() {
        SendMsgRequest msgRequest = new SendMsgRequest();
        msgRequest.setUseridList("123");
        ActionCard actionCard = new ActionCard();
        actionCard.setBtnOrientation("");
        actionCard.setMarkdown("### 三级目录");
        actionCard.setSingleTitle("单个标题");
        actionCard.setSingleUrl("单个url");
        actionCard.setTitle("标题");
        actionCard.setBtnJsonList(new ArrayList<BtnJsonList>());
        actionCard.getBtnJsonList().add(new BtnJsonList("1-url", "1-title"));
        actionCard.getBtnJsonList().add(new BtnJsonList("2-url", "2-title"));
        actionCard.getBtnJsonList().add(new BtnJsonList("3-url", "3-title"));
        msgRequest.setMsg(actionCard);

        byte[] msgProtoObj = ProtostuffUtil.serializer(msgRequest);
        System.out.println(msgProtoObj.length);
        System.out.println(new String(msgProtoObj));

        SendMsgRequest newMsgObj = ProtostuffUtil.deserializer(msgProtoObj, SendMsgRequest.class);
        System.out.println(JSON.toJSONString(newMsgObj));
    }

    /**
     * 序列化方式一：
     * 普通 java 类的序列化（内嵌对象）
     * length = 81
     * 优势：使用方便
     * 分析：序列化后数据的大小与 java 类的定义有关，如果定义了内嵌对象，包的大小会比较大
     * 说明：基于 protostuff (@Tag) 将 普通 java类 序列化
     */
    @Test
    public void testJava() {
        System.out.println("Protobuf 序列化方式一：普通 java 类的序列化（内嵌对象）");
        SendMsgRequest msgRequest = new SendMsgRequest();
        msgRequest.setUseridList("123");
        msgRequest.setMsg(new Text().setContent("hello world!"));
        byte[] msgProtoObj = ProtostuffUtil.serializer(msgRequest);
        System.out.println(msgProtoObj.length);
        System.out.println(new String(msgProtoObj));

        SendMsgRequest newMsgObj = ProtostuffUtil.deserializer(msgProtoObj, SendMsgRequest.class);
        System.out.println(JSON.toJSONString(newMsgObj));
    }

    /**
     * 序列化方式二：
     * 普通 java 类的序列化（不内嵌对象）
     * length = 19
     * 优势：使用方便
     * 分析：序列化后数据的大小与 java 类的定义有关，如果定义了内嵌对象，包的大小会比较大
     * 说明：基于 protostuff (@Tag) 将 普通 java类 序列化
     */
    @Test
    public void testJava1() {
        System.out.println("Protobuf 序列化方式二：普通 java 类的序列化（不内嵌对象）");
        SendMsgRequest1 msgRequest = new SendMsgRequest1();
        msgRequest.setUseridList("123");
        msgRequest.setContent("hello world!");
        byte[] msgProtoObj = ProtostuffUtil.serializer(msgRequest);
        System.out.println(msgProtoObj.length);
        System.out.println(new String(msgProtoObj));

        SendMsgRequest1 newMsgObj = ProtostuffUtil.deserializer(msgProtoObj, SendMsgRequest1.class);
        System.out.println(JSON.toJSONString(newMsgObj));
    }

    /**
     * 序列化方式三：
     * proto java类的序列化（protoc 生成）
     * length = 46
     * 说明：基于 protoc 命令生成的proto java类的序列化
     */
    @Test
    public void testProtoJava() {
        System.out.println("Protobuf 序列化方式三：proto java类的序列化（protoc 生成）");
        TextMsgRequestProto.TextMsgRequest.Builder builder = TextMsgRequestProto.TextMsgRequest.newBuilder();
        builder.setUseridList("123");
        builder.setContent("hello world!");

        //序列化成ProtoBuf数据结构
        byte[] msgProtoObj = ProtostuffUtil.serializer(builder.build());
        System.out.println(msgProtoObj.length);
        System.out.println(new String(msgProtoObj));

        //ProtoBuf数据结构反序列化成User对象
        TextMsgRequestProto.TextMsgRequest newUserObj = ProtostuffUtil.deserializer(msgProtoObj,
                TextMsgRequestProto.TextMsgRequest.class);
        System.out.println(newUserObj);
    }
}

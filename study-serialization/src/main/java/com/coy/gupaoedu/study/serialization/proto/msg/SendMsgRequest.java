package com.coy.gupaoedu.study.serialization.proto.msg;

import io.protostuff.Tag;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chenck
 * @date 2019/10/18 14:44
 */
@Data
public class SendMsgRequest implements Serializable {

    /**
     * 接收者的用户userid列表，最大列表长度：100
     */
    @Tag(1)
    private String useridList;

    /**
     * 接收者的部门id列表，最大列表长度：20,  接收者是部门id下(包括子部门下)的所有用户
     */
    @Tag(2)
    private String deptIdList;

    /**
     * 消息内容
     */
    @Tag(3)
    private Msg msg;

}

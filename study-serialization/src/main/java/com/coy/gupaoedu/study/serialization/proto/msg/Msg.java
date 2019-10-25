package com.coy.gupaoedu.study.serialization.proto.msg;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息父类
 *
 * @author chenck
 * @date 2019/10/18 14:51
 */
@Data
public abstract class Msg implements Serializable {

    private static final long serialVersionUID = 1L;

    // 消息类型
    public static final String ACTION_CARD = "action_card";
    public static final String FILE = "file";
    public static final String IMAGE = "image";
    public static final String LINK = "link";
    public static final String MARKDOWN = "markdown";
    public static final String OA = "oa";
    public static final String TEXT = "text";
    public static final String VOICE = "voice";

    /**
     * 限定子类必须设置消息类型
     */
    public Msg(String msgtype) {
        this.msgtype = msgtype;
    }

    /**
     * 消息类型
     */
    private String msgtype;
}

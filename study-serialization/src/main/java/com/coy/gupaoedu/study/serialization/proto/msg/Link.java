package com.coy.gupaoedu.study.serialization.proto.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 链接消息
 *
 * @author chenck
 * @date 2019/10/18 15:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Link extends Msg {

    private static final long serialVersionUID = 2727000897051708752L;

    public Link() {
        // 初始设置消息类型
        super(Msg.LINK);
    }

    private String title;
    private String text;
    private String messageUrl;
    private String picUrl;

}

package com.coy.gupaoedu.study.serialization.proto.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文本消息
 *
 * @author chenck
 * @date 2019/10/18 15:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Text extends Msg {

    private static final long serialVersionUID = -147983786705044581L;

    public Text() {
        // 初始设置消息类型
        super(Msg.TEXT);
    }

    private String content;

}

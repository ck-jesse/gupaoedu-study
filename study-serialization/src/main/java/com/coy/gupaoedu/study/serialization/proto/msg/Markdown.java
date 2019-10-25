package com.coy.gupaoedu.study.serialization.proto.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Markdown 消息
 *
 * @author chenck
 * @date 2019/10/18 15:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Markdown extends Msg {

    private static final long serialVersionUID = 4246306599802579455L;

    public Markdown() {
        // 初始设置消息类型
        super(Msg.MARKDOWN);
    }

    private String text;
    private String title;

}

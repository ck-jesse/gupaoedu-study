package com.coy.gupaoedu.study.serialization.proto.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 图片消息
 *
 * @author chenck
 * @date 2019/10/18 15:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Image extends Msg {

    private static final long serialVersionUID = -4707130696333265639L;

    public Image() {
        // 初始设置消息类型
        super(Msg.IMAGE);
    }

    private String mediaId;

}

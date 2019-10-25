package com.coy.gupaoedu.study.serialization.proto.msg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 卡片消息
 *
 * @author chenck
 * @date 2019/10/18 15:03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ActionCard extends Msg {

    private static final long serialVersionUID = 4698844407601078472L;

    public ActionCard() {
        // 初始设置消息类型
        super(Msg.ACTION_CARD);
    }

    private List<BtnJsonList> btnJsonList;
    private String btnOrientation;
    private String markdown;
    private String singleTitle;
    private String singleUrl;
    private String title;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BtnJsonList {
        private String actionUrl;
        private String title;
    }
}

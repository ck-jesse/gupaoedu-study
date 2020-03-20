package com.coy.gupaoedu.study.jvm;

import lombok.Data;

/**
 * @author chenck
 * @date 2020/3/19 10:51
 */
@Data
public class User {
    public User() {

    }

    public User(String nickname) {
        this.nickname = nickname;
    }

    //买家Id
    private String userId;
    //买家昵称
    private String nickname;
    //订单金额
    private Long actualAmount;
    //订单号
    private String orderNo;

}

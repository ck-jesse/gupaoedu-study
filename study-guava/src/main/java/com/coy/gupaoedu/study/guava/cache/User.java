package com.coy.gupaoedu.study.guava.cache;

import com.ck.platform.common.util.RandomUtil;
import lombok.Data;

/**
 * @author chenck
 * @date 2020/3/19 10:51
 */
@Data
public class User {
    //买家Id
    private String userId;
    //买家昵称
    private String nickname;
    //订单金额
    private Long actualAmount;
    //订单号
    private String orderNo;

    public static User init() {
        User user = new User();
        user.setUserId(RandomUtil.genRandomNumber(4));
        return user;
    }
}

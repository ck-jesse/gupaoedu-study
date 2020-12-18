package com.coy.gupaoedu.study.spring.easyexcel.weeget.coupon;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * 用户id
 *
 * @author chenck
 * @date 2020/12/18 18:38
 */
@Data
public class UserCouponData {

    @ColumnWidth(20)
    @ExcelProperty("weixin_user_id")
    private Integer weixin_user_id;
}

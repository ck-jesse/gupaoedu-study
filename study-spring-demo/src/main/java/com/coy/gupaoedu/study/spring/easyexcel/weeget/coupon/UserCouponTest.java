package com.coy.gupaoedu.study.spring.easyexcel.weeget.coupon;

import com.alibaba.excel.EasyExcel;
import org.junit.Test;

/**
 * @author chenck
 * @date 2020/12/18 20:12
 */
public class UserCouponTest {

    @Test
    public void getStock01() throws InterruptedException {
        String fileName = "E:/temp/coupon/派发优惠券用户id汇总.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, UserCouponData.class, new ReloadUserCouponCacheListener()).sheet().doRead();

        while (true) {
            Thread.sleep(1000);
        }
    }
}

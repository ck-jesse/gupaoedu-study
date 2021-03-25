package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenck
 * @date 2021/3/23 21:55
 */
@Data
public class Price0Data {
    private String update_date;
    private Integer goods_spec_id;
    private BigDecimal supply_price;
    private BigDecimal cost_price;
    private BigDecimal lowest_price;

}

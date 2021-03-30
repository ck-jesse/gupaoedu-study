package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenck
 * @date 2021/3/25 20:04
 */
@Data
public class OrderData {
    private Integer order_id;
    private String order_sn;
    private Integer order_goods_id;
    private Integer goods_spec_id;
    private BigDecimal platform_price;
    private Integer goods_spec_snapshot_id;
    private Integer g_goods_spec_snapshot_id;
    private Integer g_goods_spec_id;
    private BigDecimal lowest_price;
    private BigDecimal highest_price;
    private BigDecimal market_price;
    private BigDecimal supply_price;
    private BigDecimal cost_price;
}

package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenck
 * @date 2021/3/23 20:24
 */
@Data
public class WarehouseSpec {

    private Integer warehouse_spec_id;
    private Integer goods_spec_id;
    private Integer warehouse_id;
    private BigDecimal price_one;
    private BigDecimal price_two;
    private BigDecimal price_three;
    private BigDecimal price_four;
    private BigDecimal price_five;
    private BigDecimal price_six;
    private BigDecimal price_seven;
    private BigDecimal price_eight;
    private BigDecimal price_nine;
    private BigDecimal price_ten;
    private Integer stock;
    private Integer sales;
    private Integer threshold;
    private Integer stock_alert;
    private Integer sort_order;
    private Integer state;
    private Integer organization_id;
    private Integer stock_early_warning;
    private Integer locking_stock;
    private Integer lock_tag;
    private Integer version;

}

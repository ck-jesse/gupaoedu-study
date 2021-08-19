package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenck
 * @date 2021/3/22 12:28
 */
@Data
public class GoodsSpecSnapshot {
    private Long goods_spec_snapshot_id;
    private Integer goods_spec_id;
    private Integer goods_id;
    private String supplier_id;
    private String goods_number;
    private String spec_number;
    private String bar_code;
    private String spec_one;
    private String spec_two;
    private String spec_three;
    private Integer sort_order;
    private Integer goods_status;
    private String spec_picture_url;
    private BigDecimal supply_price;
    private BigDecimal lowest_price;
    private BigDecimal highest_price;
    private BigDecimal cost_price;
    private BigDecimal market_price;
    private Long add_time;
    private Long update_time;
    private Integer version;
    private Long goods_snapshot_id;


}

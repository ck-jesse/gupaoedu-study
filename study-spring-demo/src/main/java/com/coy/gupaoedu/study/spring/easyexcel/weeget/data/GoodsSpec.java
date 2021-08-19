package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenck
 * @date 2021/3/22 12:28
 */
@Data
public class GoodsSpec {
    private Integer goods_spec_id;
    private Integer goods_id;
    private String spec_number;
    private String bar_code;
    private String spec_one;
    private String spec_two;
    private String spec_three;
    private Integer sort_order;
    private Integer state;
    private Integer group_spec_one_id;
    private Integer group_spec_two_id;
    private BigDecimal supply_price;
    private BigDecimal lowest_price;
    private BigDecimal highest_price;
    private BigDecimal cost_price;
    private BigDecimal market_price;
    private Long goods_spec_snapshot_id;

}

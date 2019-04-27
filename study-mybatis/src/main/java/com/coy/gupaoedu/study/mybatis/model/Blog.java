package com.coy.gupaoedu.study.mybatis.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenck
 * @date 2019/4/27 21:15
 */
@Data
public class Blog {

    private Long id;
    private String spid;
    private String goods_name;
    private BigDecimal goods_price;
    private Integer state;
}

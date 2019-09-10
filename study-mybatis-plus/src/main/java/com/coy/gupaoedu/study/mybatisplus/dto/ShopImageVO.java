package com.coy.gupaoedu.study.mybatisplus.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 商铺图片
 *
 * @author chenck
 * @date 2019/9/6 12:17
 */
@Data
public class ShopImageVO implements Serializable {
    //主键生成方式
    private Long id;
    // 商家ID
    private String vendorId;
    // 图片名称
    private String name;
    // 用于存储外部标识，如存储在阿里云oss上对应的图片标识
    private String outId;
    // 图片名称
    private Integer size;
    // 图片长度
    private Integer length;
    // 图片宽度
    private Integer width;
    // 图片链接
    private String imgUrl;
    // 逻辑删除标志 0:正常 1:删除
    private Integer isDeleted;
    private Long createTime;
    private Long updateTime;
}

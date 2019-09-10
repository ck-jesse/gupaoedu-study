package com.coy.gupaoedu.study.mybatisplus.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 图片
 *
 * @author chenck
 * @date 2019/9/6 12:17
 */
@Data
@TableName("t_shop_image")
public class ShopImageEntity {
    //主键生成方式
    @TableId(value = "id", type = IdType.AUTO)
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

    //默认插入
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    // 逻辑删除标志 0:正常 1:删除
    @TableLogic
    private Integer isDeleted;
}

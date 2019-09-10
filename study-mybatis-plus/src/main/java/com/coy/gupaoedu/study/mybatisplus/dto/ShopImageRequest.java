package com.coy.gupaoedu.study.mybatisplus.dto;

import com.coy.gupaoedu.study.mybatisplus.common.beanvalidation.annotation.BVLength;
import com.coy.gupaoedu.study.mybatisplus.common.domain.BasePageRequest;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


/**
 * 商铺图片
 *
 * @author chenck
 * @date 2019/9/6 12:17
 */
@Data
public class ShopImageRequest extends BasePageRequest {
    //主键生成方式
    private Long id;
    // 商家ID
    private String vendorId;
    // 图片名称
    @NotBlank(message = "请传入图片名称")
    private String name;

    // 用于存储外部标识，如存储在阿里云oss上对应的图片标识
    @NotBlank(message = "请传入外部存储标识（如阿里云oss）")
    @BVLength(max = 64, message = "外部存储标识最大长度64")
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
}

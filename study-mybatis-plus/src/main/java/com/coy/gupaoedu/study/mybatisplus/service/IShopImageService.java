package com.coy.gupaoedu.study.mybatisplus.service;


import com.coy.gupaoedu.study.mybatisplus.common.domain.DataResponse;
import com.coy.gupaoedu.study.mybatisplus.common.domain.PageResponse;
import com.coy.gupaoedu.study.mybatisplus.dto.ShopImageRequest;
import com.coy.gupaoedu.study.mybatisplus.dto.ShopImageVO;

import java.util.List;

/**
 * 店铺图片空间服务
 *
 * @author chenck
 * @date 2019/9/6 16:48
 */
public interface IShopImageService {

    DataResponse<ShopImageVO> selectShopImage(Long id);
    /**
     * 保存店铺图片（支持新增和修改）
     */
    DataResponse<Long> saveOrUpdateShopImage(ShopImageRequest request);

    /**
     * 查询店铺图片列表
     */
    PageResponse<ShopImageVO> selectShopImageList(ShopImageRequest request);

    /**
     * 批量删除图片（逻辑删除）
     */
    DataResponse deleteBatchShopImage(List<Long> imgIds);
}

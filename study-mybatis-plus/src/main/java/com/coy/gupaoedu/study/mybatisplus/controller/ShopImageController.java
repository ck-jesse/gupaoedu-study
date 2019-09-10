package com.coy.gupaoedu.study.mybatisplus.controller;

import com.coy.gupaoedu.study.mybatisplus.common.consts.ResponseCodeEnum;
import com.coy.gupaoedu.study.mybatisplus.common.domain.DataResponse;
import com.coy.gupaoedu.study.mybatisplus.common.domain.PageResponse;
import com.coy.gupaoedu.study.mybatisplus.dto.ShopImageRequest;
import com.coy.gupaoedu.study.mybatisplus.dto.ShopImageVO;
import com.coy.gupaoedu.study.mybatisplus.service.IShopImageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author chenck
 * @date 2019/9/6 18:06
 */
@RestController
@RequiredArgsConstructor
public class ShopImageController {

    @Autowired
    private IShopImageService shopImageService;

    /**
     * 查询店铺图片列表
     */
    @PostMapping(value = "/shop/image/select/list")
    public PageResponse<ShopImageVO> selectShopImageList(@RequestBody ShopImageRequest request) {
        return shopImageService.selectShopImageList(request);
    }

    /**
     * 保存店铺图片（支持新增和修改）
     * 注：id 为空或不存在，则新增，id存在，则修改
     */
    @PostMapping("/shop/image/save")
    public DataResponse<Long> saveOrUpdateShopImage(@RequestBody @Valid ShopImageRequest request) {
        return shopImageService.saveOrUpdateShopImage(request);
    }

    /**
     * 批量删除图片
     */
    @PostMapping(value = "/shop/image/delete/batch")
    public DataResponse<Long> deleteBatchShopImage(@RequestBody List<Long> imgIdList) {
        if (CollectionUtils.isEmpty(imgIdList)) {
            return new DataResponse<Long>(ResponseCodeEnum.ERROR_PARAM, "请传入图片id");
        }
        return shopImageService.deleteBatchShopImage(imgIdList);
    }

}

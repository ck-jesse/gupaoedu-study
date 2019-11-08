package com.coy.gupaoedu.study.mybatisplus.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coy.gupaoedu.study.mybatisplus.common.consts.ResponseCodeEnum;
import com.coy.gupaoedu.study.mybatisplus.common.domain.DataResponse;
import com.coy.gupaoedu.study.mybatisplus.common.domain.PageResponse;
import com.coy.gupaoedu.study.mybatisplus.common.util.BeanMapper;
import com.coy.gupaoedu.study.mybatisplus.dao.entity.ShopImageEntity;
import com.coy.gupaoedu.study.mybatisplus.dao.mapper.ShopImageMapper;
import com.coy.gupaoedu.study.mybatisplus.dao.service.ShopImageService;
import com.coy.gupaoedu.study.mybatisplus.dto.ShopImageRequest;
import com.coy.gupaoedu.study.mybatisplus.dto.ShopImageVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 店铺图片空间服务实现
 *
 * @author chenck
 * @date 2019/9/6 16:57
 */
@Service
public class ShopImageServiceImpl implements IShopImageService {

    @Autowired
    private ShopImageMapper shopImageMapper;

    @Autowired
    private ShopImageService shopImageService;

    @Override
    public DataResponse<ShopImageVO> selectShopImage(Long id) {
        ShopImageEntity shopImageEntity = shopImageMapper.selectById(id);
        if (null == shopImageEntity) {
            return new DataResponse<>();
        }
        ShopImageVO shopImageVO = new ShopImageVO();
        BeanUtils.copyProperties(shopImageEntity, shopImageVO);
        return new DataResponse<>(shopImageVO);
    }

    @Override
    public DataResponse<Long> saveOrUpdateShopImage(ShopImageRequest request) {
        ShopImageEntity entity = new ShopImageEntity();
        BeanUtils.copyProperties(request, entity);
        boolean rslt = shopImageService.saveOrUpdate(entity);
        if (!rslt) {
            return new DataResponse<Long>().error("保存图片信息失败");
        }
        return new DataResponse<>(entity.getId());
    }

    @Override
    public PageResponse<ShopImageVO> selectShopImageList(ShopImageRequest request) {
        if (null == request.getIsDeleted()) {
            request.setIsDeleted(0);
        }
        // 分页查询设置
        Page page = PageHelper.startPage(request.getPageNum(), request.getPageSize());
        page.setCount(request.getPageNum() <= 1);// 注意:不执行count语句时，total的值为-1

        // 默认查询所有字段
        List<ShopImageEntity> resList = shopImageMapper.selectList(Wrappers.<ShopImageEntity>lambdaQuery()
                // 构建查询条件
                .eq(ShopImageEntity::getVendorId, request.getVendorId())
                .eq(ShopImageEntity::getIsDeleted, request.getIsDeleted())
        );
        List<ShopImageVO> imageVOList = BeanMapper.mapList(resList, ShopImageVO.class);
        return new PageResponse<>(imageVOList, ((com.github.pagehelper.Page) resList).getTotal());
    }

    /**
     * 基于mybatis-plus实现逻辑删除，必须开启实例化 LogicSqlInjector + @TableLogic
     */
    @Override
    public DataResponse deleteBatchShopImage(List<Long> imgIds) {
        int rslt = shopImageMapper.deleteBatchIds(imgIds);
        return new DataResponse(ResponseCodeEnum.SUCCESS, "受影响行数为" + rslt);
    }
}

package com.coy.gupaoedu.study.mybatisplus;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.coy.gupaoedu.study.mybatisplus.dao.entity.MemberIntegralRecordEntity;
import com.coy.gupaoedu.study.mybatisplus.dao.mapper.MemberIntegralRecordMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.List;

/**
 * @author chenck
 * @date 2019/9/26 20:36
 */
@Component
public class RefreshRedisCacheTestService implements InitializingBean {

    @Resource
    private MemberIntegralRecordMapper memberIntegralRecordMapper;


    @Override
    public void afterPropertiesSet() throws Exception {

        String cardCode = "2000136075";
        Integer month = 9;
//        String key = ProgressCacheUtils.getMallPvCacheHashKey(cardCode);
//        String field = ProgressCacheUtils.getMallPvCacheFieldKey(cardCode, month);

        // 默认查询所有字段
        List<MemberIntegralRecordEntity> resList = memberIntegralRecordMapper.selectList(Wrappers.<MemberIntegralRecordEntity>lambdaQuery()
                // 构建查询条件
                .eq(MemberIntegralRecordEntity::getCardCode, cardCode)
        );

        // 打印
        resList.stream().forEach(System.out::println);
    }
}

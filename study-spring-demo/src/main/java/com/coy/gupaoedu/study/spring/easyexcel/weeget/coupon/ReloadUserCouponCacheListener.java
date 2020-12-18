package com.coy.gupaoedu.study.spring.easyexcel.weeget.coupon;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.coy.gupaoedu.study.spring.common.ThreadPoolSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 有个很重要的点 ReloadStockCacheListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 *
 * @author chenck
 * @date 2020/10/13 14:36
 */
public class ReloadUserCouponCacheListener extends AnalysisEventListener<UserCouponData> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReloadUserCouponCacheListener.class);

    private static final ThreadPoolExecutor executor = ThreadPoolSupport.getPool(5, 5, 30L, 100000);

    private AtomicLong totalCount = new AtomicLong();// 总数
    private AtomicLong dealCount = new AtomicLong();// 处理数量
    private AtomicLong refreshCount = new AtomicLong();// 刷新数量

    /**
     * 每一条数据解析都会来调用
     *
     * @param context
     */
    @Override
    public void invoke(UserCouponData data, AnalysisContext context) {
        totalCount.incrementAndGet();
        executor.execute(() -> {
            UserCouponService userCouponService = new UserCouponService(totalCount, dealCount, refreshCount);
            userCouponService.reloadUserCouponCache(data);
        });
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        LOGGER.info("所有数据解析完成 totalCount={} dealCount={} refreshCount={}", totalCount.get(), dealCount.get(), refreshCount.get());
    }

}

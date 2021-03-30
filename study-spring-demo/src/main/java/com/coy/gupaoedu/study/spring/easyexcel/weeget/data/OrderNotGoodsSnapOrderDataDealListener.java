package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class OrderNotGoodsSnapOrderDataDealListener extends AnalysisEventListener<Price0Data> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderNotGoodsSnapOrderDataDealListener.class);

    private AtomicLong totalCount = new AtomicLong();// 总数
    private AtomicLong dealCount = new AtomicLong();// 处理数量
    private AtomicLong refreshCount = new AtomicLong();// 刷新数量


    private Integer minGoodsSpecId = 64896000;// 修复数据的起始规格id
    private Integer maxGoodsSpecId = 64910000;// 修复数据的结束规格id

    private static final List<String> sqlList = new ArrayList<>();
    private static final List<String> rollbackSqlList = new ArrayList<>();

    /**
     * 每一条数据解析都会来调用
     *
     * @param context
     */
    @Override
    public void invoke(Price0Data price0Data, AnalysisContext context) {
        totalCount.incrementAndGet();

        Map<Integer, GoodsSpec> goodsSpecMap = GoodsSpecListener.getGoodsSpecMap();
        GoodsSpec goodsSpec = goodsSpecMap.get(price0Data.getGoods_spec_id());
        if (null == goodsSpec) {
            LOGGER.info("goodsSpecId={} 未找到对应规格信息", price0Data.getGoods_spec_id(), price0Data);
            return;
        }
        orderNotGoodsSnap(price0Data, goodsSpec);
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        LOGGER.info("所有数据解析完成！ totalCount={}", totalCount.get());
    }

    /**
     * 订单没有快照的处理
     */
    private void orderNotGoodsSnap(Price0Data price0Data, GoodsSpec goodsSpec) {
        minGoodsSpecId++;
        StringBuilder insertSql = new StringBuilder("INSERT INTO `goods_spec`( ");
        insertSql.append("`goods_spec_id`, ");
        insertSql.append("`goods_id`, ");
        insertSql.append("`spec_number`, ");
        insertSql.append("`bar_code`, ");
        insertSql.append("`spec_one`, ");
        insertSql.append("`spec_two`, ");
        insertSql.append("`spec_three`, ");
        insertSql.append("`sort_order`, ");
        insertSql.append("`state`, ");
        insertSql.append("`group_spec_one_id`, ");
        insertSql.append("`group_spec_two_id`, ");
        insertSql.append("`supply_price`, ");
        insertSql.append("`lowest_price`, ");
        insertSql.append("`highest_price`, ");
        insertSql.append("`cost_price`, ");
        insertSql.append("`market_price`");
        insertSql.append(") VALUES (");
        insertSql.append(minGoodsSpecId).append(",");
        insertSql.append(goodsSpec.getGoods_id()).append(",");
        insertSql.append("\"").append(null == goodsSpec.getSpec_number() ? "" : goodsSpec.getSpec_number()).append("\",");
        insertSql.append("\"").append(null == goodsSpec.getBar_code() ? "" : goodsSpec.getBar_code()).append("\",");
        insertSql.append("\"").append(null == goodsSpec.getSpec_one() ? "" : goodsSpec.getSpec_one()).append("\",");
        insertSql.append("\"").append(null == goodsSpec.getSpec_two() ? "" : goodsSpec.getSpec_two()).append("\",");
        insertSql.append("\"").append(null == goodsSpec.getSpec_three() ? "" : goodsSpec.getSpec_three()).append("\",");
        insertSql.append(goodsSpec.getSort_order()).append(",");
        insertSql.append("0").append(",");
        insertSql.append(null == goodsSpec.getGroup_spec_one_id() ? 0 : goodsSpec.getGroup_spec_one_id()).append(",");
        insertSql.append(null == goodsSpec.getGroup_spec_two_id() ? 0 : goodsSpec.getGroup_spec_two_id()).append(",");

        insertSql.append(price0Data.getSupply_price()).append(",");
        insertSql.append(price0Data.getLowest_price()).append(",");
        insertSql.append(goodsSpec.getHighest_price()).append(",");
        insertSql.append(price0Data.getCost_price()).append(",");
        insertSql.append(goodsSpec.getMarket_price());
        insertSql.append(");");

        // 更新订单的规格id为新的规格id
        StringBuilder updateSql = new StringBuilder("update order_goods set ");
        updateSql.append(" goods_spec_id=").append(minGoodsSpecId);
        updateSql.append(" , remark=\"").append(goodsSpec.getGoods_spec_id()).append("\"");// 备份旧的规格id，用于追溯
        updateSql.append(" where goods_spec_snapshot_id=0 and goods_spec_id=").append(goodsSpec.getGoods_spec_id()).append(";");

        // 还原订单的规格id
        StringBuilder rockbackSql = new StringBuilder("update order_goods set ");
        rockbackSql.append(" goods_spec_id=").append(goodsSpec.getGoods_spec_id());
        rockbackSql.append(" , remark=\"").append(minGoodsSpecId).append("\"");
        rockbackSql.append(" where goods_spec_snapshot_id=0 and goods_spec_id=").append(minGoodsSpecId).append(";");

        LOGGER.info("goodsSpecId={} insert_sql={}", goodsSpec.getGoods_spec_id(), insertSql.toString());
        LOGGER.info("goodsSpecId={} update_sql={}", goodsSpec.getGoods_spec_id(), updateSql.toString());
        LOGGER.info("goodsSpecId={} rockbacksql={}", goodsSpec.getGoods_spec_id(), rockbackSql.toString());

        sqlList.add(insertSql.toString());
        sqlList.add(updateSql.toString());
        rollbackSqlList.add(rockbackSql.toString());
    }

    public static List<String> getSqlList() {
        return sqlList;
    }

    public static List<String> getRollbackSqlList() {
        return rollbackSqlList;
    }
}

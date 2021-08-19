package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author chenck
 * @date 2021/3/25 20:06
 */
public class OrderDataListener extends AnalysisEventListener<OrderData> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDataListener.class);

    private AtomicLong totalCount = new AtomicLong();// 总数

    private static Integer minGoodsSpecId = 64900000;// 修复数据的起始规格id 上次使用到了 64898553
//    private static Integer minGoodsSpecId = 64902000;// 修复数据的起始规格id 上次使用到了 64901666
    private static Integer maxGoodsSpecId = 64910000;// 修复数据的结束规格id

    private static final List<String> sqlList = new ArrayList<>();
    private static final List<String> rollbackSqlList = new ArrayList<>();

    private static final List<OrderData> orderDataList = new ArrayList<>();
    private static final Map<Integer, OrderData> orderDataMap = new HashMap<>();

    @Override
    public void invoke(OrderData orderData, AnalysisContext analysisContext) {
        totalCount.incrementAndGet();
        orderDataList.add(orderData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        LOGGER.info("所有数据解析完成！ totalCount={}", totalCount.get());
    }

    /**
     * 过滤掉重复的规格
     */
    public static Map<Integer, OrderData> getOrderDataMap() {
        if (orderDataMap.size() > 0) {
            return orderDataMap;
        }
        // 等于是去重了
        Map<Integer, OrderData> orderDataMap1 = orderDataList.stream().collect(HashMap::new, (map, orderData) -> map.put(orderData.getGoods_spec_id(), orderData), HashMap::putAll);
        orderDataMap.putAll(orderDataMap1);
        return orderDataMap;
    }

    /**
     * 比较规格对应的快照id,金额是否一致
     */
    public static void compareSpecSnapshotPrice() {
        Map<Integer, List<OrderData>> orderDataMap = orderDataList.stream().collect(Collectors.groupingBy(OrderData::getGoods_spec_id));

        orderDataMap.forEach((goodsSpecId, orderDataList) -> {
            if (orderDataList.size() == 1) {
                LOGGER.info("goodsSpecId={} 规格只有一条记录无需比对", goodsSpecId);
                return;
            }
            for (OrderData orderData1 : orderDataList) {
                for (OrderData orderData2 : orderDataList) {
                    if (orderData1.equals(orderData2)) {
                        continue;
                    }
                    if (!orderData1.getGoods_spec_snapshot_id().equals(orderData2.getGoods_spec_snapshot_id())) {
                        LOGGER.info("goodsSpecId={} 规格对应的规格快照id不一致 [1]{} [2]{}", goodsSpecId, orderData1.getGoods_spec_snapshot_id(), orderData2.getGoods_spec_snapshot_id());
                        continue;
                    }
                    if (!orderData1.getSupply_price().equals(orderData2.getSupply_price())) {
                        LOGGER.info("goodsSpecId={} 供货价不一样 [1]{} [2]{}", goodsSpecId, orderData1.getSupply_price(), orderData2.getSupply_price());
                        continue;
                    }
                    if (!orderData1.getLowest_price().equals(orderData2.getLowest_price())) {
                        LOGGER.info("goodsSpecId={} 最低销售价不一样 [1]{} [2]{}", goodsSpecId, orderData1.getLowest_price(), orderData2.getLowest_price());
                        continue;
                    }
                    if (!orderData1.getCost_price().equals(orderData2.getCost_price())) {
                        LOGGER.info("goodsSpecId={} 成本价不一样 [1]{} [2]{}", goodsSpecId, orderData1.getCost_price(), orderData2.getCost_price());
                        continue;
                    }
                    if (!orderData1.getMarket_price().equals(orderData2.getMarket_price())) {
                        LOGGER.info("goodsSpecId={} 市场价不一样 [1]{} [2]{}", goodsSpecId, orderData1.getMarket_price(), orderData2.getMarket_price());
                        continue;
                    }
                }
            }
        });
    }

    /**
     * 构建修复SQL
     */
    public static void buildRepairSql() {
        Map<Integer, OrderData> orderDataMap = getOrderDataMap();

        Map<Integer, GoodsSpec> goodsSpecMap = GoodsSpecListener.getGoodsSpecMap();

        orderDataMap.forEach((goodsSpecId, orderData) -> {
            GoodsSpec goodsSpec = goodsSpecMap.get(goodsSpecId);
            if (null == goodsSpec) {
                LOGGER.info("goodsSpecId={} 没有找对应的规格信息", goodsSpecId);
                return;
            }
            orderNotGoodsSnap(orderData, goodsSpec);
        });
    }

    /**
     * 订单没有快照的处理
     */
    private static void orderNotGoodsSnap(OrderData orderData, GoodsSpec goodsSpec) {
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

        insertSql.append(orderData.getSupply_price()).append(",");
        insertSql.append(orderData.getLowest_price()).append(",");
        insertSql.append(orderData.getHighest_price()).append(",");
        insertSql.append(orderData.getCost_price()).append(",");
        insertSql.append(orderData.getMarket_price());
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

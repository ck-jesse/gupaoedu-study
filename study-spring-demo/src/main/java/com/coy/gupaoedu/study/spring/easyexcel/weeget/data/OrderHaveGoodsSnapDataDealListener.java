package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class OrderHaveGoodsSnapDataDealListener extends AnalysisEventListener<GoodsSpecSnapshot> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderHaveGoodsSnapDataDealListener.class);

    private AtomicLong totalCount = new AtomicLong();// 总数
    private AtomicLong dealCount = new AtomicLong();// 处理数量
    private AtomicLong refreshCount = new AtomicLong();// 刷新数量

    private Integer minGoodsSpecId = 64891000;// 修复数据的起始规格id
    private Integer maxGoodsSpecId = 64910000;// 修复数据的结束规格id

    private Integer minWarehouseSpecId = 64520000;// 修复数据的起始仓库规格id
    private Integer maxWarehouseSpecId = 64530000;// 修复数据的结束仓库规格id

    /**
     * 每一条数据解析都会来调用
     *
     * @param context
     */
    @Override
    public void invoke(GoodsSpecSnapshot specSnapshot, AnalysisContext context) {
        totalCount.incrementAndGet();

        specSnapshot.setHighest_price(specSnapshot.getHighest_price().setScale(2));
        specSnapshot.setLowest_price(specSnapshot.getLowest_price().setScale(2));
        specSnapshot.setMarket_price(specSnapshot.getMarket_price().setScale(2));
        specSnapshot.setSupply_price(specSnapshot.getSupply_price().setScale(2));
        specSnapshot.setCost_price(specSnapshot.getCost_price().setScale(2));

        /**
         * 改价规格id列表，同时有下单的规格
         */
        Map<Integer, GoodsSpec> goodsSpecMap = GoodsSpecListener.getGoodsSpecMap();

        GoodsSpec goodsSpec = goodsSpecMap.get(specSnapshot.getGoods_spec_id());
        if (null == goodsSpec) {
            LOGGER.info("goodsSpecId={} 根据快照中的规格id未找到对应的规格信息", specSnapshot.getGoods_spec_id());
            return;
        }

        dealCount.incrementAndGet();
        goodsSpec.setHighest_price(goodsSpec.getHighest_price().setScale(2));
        goodsSpec.setLowest_price(goodsSpec.getLowest_price().setScale(2));
        goodsSpec.setMarket_price(goodsSpec.getMarket_price().setScale(2));
        goodsSpec.setSupply_price(goodsSpec.getSupply_price().setScale(2));


        minGoodsSpecId++;
        minWarehouseSpecId++;

        orderHaveGoodsSnap(specSnapshot, goodsSpec);
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


    private void orderHaveGoodsSnap(GoodsSpecSnapshot specSnapshot, GoodsSpec goodsSpec) {
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
        insertSql.append(null == goodsSpec.getSort_order() ? 0 : goodsSpec.getSort_order()).append(",");
        insertSql.append("0").append(",");
        insertSql.append(null == goodsSpec.getGroup_spec_one_id() ? 0 : goodsSpec.getGroup_spec_one_id()).append(",");
        insertSql.append(null == goodsSpec.getGroup_spec_two_id() ? 0 : goodsSpec.getGroup_spec_two_id()).append(",");
        // 取快照里面的供应商价格和最低销售价
        insertSql.append(specSnapshot.getSupply_price()).append(",");
        insertSql.append(specSnapshot.getLowest_price()).append(",");
        insertSql.append(goodsSpec.getHighest_price()).append(",");
        insertSql.append(goodsSpec.getCost_price()).append(",");
        insertSql.append(goodsSpec.getMarket_price());
        insertSql.append(");");

        // 更新订单中的规格id为新的规格id
        StringBuilder updateSql = new StringBuilder("update order_goods set ");
        updateSql.append(" goods_spec_id=").append(minGoodsSpecId);
        updateSql.append(" , remark=\"").append(goodsSpec.getGoods_spec_id()).append("\"");// 备份旧的规格id，用于追溯
        updateSql.append(" where goods_spec_snapshot_id=0 and goods_spec_id=").append(goodsSpec.getGoods_spec_id()).append(";");

        // 还原订单中的规格id
        StringBuilder rockbackSql = new StringBuilder("update order_goods set ");
        rockbackSql.append(" goods_spec_id=").append(goodsSpec.getGoods_spec_id());
        rockbackSql.append(" , remark=\"").append(minGoodsSpecId).append("\"");
        rockbackSql.append(" where goods_spec_snapshot_id=0 and goods_spec_id=").append(minGoodsSpecId).append(";");


        // 更新快照中的规格id为新的规格id
        StringBuilder updateSnapshotSql = new StringBuilder("update goods_spec_snapshot set ");
        updateSnapshotSql.append(" goods_spec_id=").append(minGoodsSpecId);
        updateSnapshotSql.append(" where goods_spec_snapshot_id=").append(goodsSpec.getGoods_spec_snapshot_id());
        updateSnapshotSql.append(" and goods_spec_id=").append(goodsSpec.getGoods_spec_id()).append(";");

        // 还原快照中的规格id
        StringBuilder rockbackSnapshotSql = new StringBuilder("update goods_spec_snapshot set ");
        rockbackSnapshotSql.append(" goods_spec_id=").append(goodsSpec.getGoods_spec_id());
        rockbackSnapshotSql.append(" where goods_spec_snapshot_id=").append(goodsSpec.getGoods_spec_snapshot_id());
        rockbackSnapshotSql.append(" and goods_spec_id=").append(minGoodsSpecId).append(";");


        LOGGER.info("goodsSpecId={} insert_sql={}", goodsSpec.getGoods_spec_id(), insertSql.toString());
        LOGGER.info("goodsSpecId={} update_sql={}", goodsSpec.getGoods_spec_id(), updateSql.toString());
        LOGGER.info("goodsSpecId={} rockbacksql={}", goodsSpec.getGoods_spec_id(), rockbackSql.toString());
        LOGGER.info("goodsSpecId={} snapshotsql_update_sql={}", goodsSpec.getGoods_spec_id(), updateSnapshotSql.toString());
        LOGGER.info("goodsSpecId={} snapshotsql_rockbacksql={}", goodsSpec.getGoods_spec_id(), rockbackSnapshotSql.toString());

        buildWarehouseSpecSql(goodsSpec);
    }

    private void buildWarehouseSpecSql(GoodsSpec goodsSpec) {
        Map<Integer, WarehouseSpec> warehouseSpecMap = WarehouseSpecListener.getWarehouseSpecMap();
        WarehouseSpec warehouseSpec = warehouseSpecMap.get(goodsSpec.getGoods_spec_id());
        if (null == warehouseSpec) {
            LOGGER.info("goodsSpecId={} 没有找到规格的库存信息", goodsSpec.getGoods_spec_id());
            return;
        }

        StringBuilder insertSql = new StringBuilder("INSERT INTO `warehouse_spec`( ");
        insertSql.append("`warehouse_spec_id`, ");
        insertSql.append("`goods_spec_id`, ");
        insertSql.append("`warehouse_id`, ");
        insertSql.append("`price_one`, ");
        insertSql.append("`price_two`, ");
        insertSql.append("`price_three`, ");
        insertSql.append("`price_four`, ");
        insertSql.append("`price_five`, ");
        insertSql.append("`price_six`, ");
        insertSql.append("`price_seven`, ");
        insertSql.append("`price_eight`, ");
        insertSql.append("`price_nine`, ");
        insertSql.append("`price_ten`, ");
        insertSql.append("`stock`, ");
        insertSql.append("`sales`, ");
        insertSql.append("`threshold`, ");
        insertSql.append("`stock_alert`, ");
        insertSql.append("`sort_order`, ");
        insertSql.append("`state`, ");
        insertSql.append("`organization_id`, ");
        insertSql.append("`stock_early_warning`, ");
        insertSql.append("`locking_stock`, ");
        insertSql.append("`version`, ");
        insertSql.append("`lock_tag` ");
        insertSql.append(") VALUES (");
        insertSql.append(minWarehouseSpecId).append(",");
        insertSql.append(minGoodsSpecId).append(",");
        insertSql.append(warehouseSpec.getWarehouse_id()).append(",");
        insertSql.append(warehouseSpec.getPrice_one()).append(",");
        insertSql.append(warehouseSpec.getPrice_two()).append(",");
        insertSql.append(warehouseSpec.getPrice_three()).append(",");
        insertSql.append(warehouseSpec.getPrice_four()).append(",");
        insertSql.append(warehouseSpec.getPrice_five()).append(",");
        insertSql.append(warehouseSpec.getPrice_six()).append(",");
        insertSql.append(warehouseSpec.getPrice_seven()).append(",");
        insertSql.append(warehouseSpec.getPrice_eight()).append(",");
        insertSql.append(warehouseSpec.getPrice_nine()).append(",");
        insertSql.append(warehouseSpec.getPrice_ten()).append(",");
        insertSql.append(warehouseSpec.getStock()).append(",");
        insertSql.append(null == warehouseSpec.getSales() ? 0 : warehouseSpec.getSales()).append(",");
        insertSql.append(null == warehouseSpec.getThreshold() ? 0 : warehouseSpec.getThreshold()).append(",");
        insertSql.append(null == warehouseSpec.getStock_alert() ? 0 : warehouseSpec.getStock_alert()).append(",");
        insertSql.append(warehouseSpec.getSort_order()).append(",");
        insertSql.append(warehouseSpec.getState()).append(",");
        insertSql.append(warehouseSpec.getOrganization_id()).append(",");
        insertSql.append(null == warehouseSpec.getStock_early_warning() ? 0 : warehouseSpec.getStock_early_warning()).append(",");
        insertSql.append(warehouseSpec.getLocking_stock()).append(",");
        insertSql.append(warehouseSpec.getVersion()).append(",");
        insertSql.append(warehouseSpec.getLock_tag());
        insertSql.append(");");
        LOGGER.info("goodsSpecId={} warehouseSpec_insert_sql={}", goodsSpec.getGoods_spec_id(), insertSql.toString());
    }
}

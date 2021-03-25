package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class OrderNotGoodsSnapDataDealListener extends AnalysisEventListener<ImportDataItem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderNotGoodsSnapDataDealListener.class);

    private AtomicLong totalCount = new AtomicLong();// 总数
    private AtomicLong dealCount = new AtomicLong();// 处理数量
    private AtomicLong refreshCount = new AtomicLong();// 刷新数量


    private Integer minGoodsSpecId = 64890000;// 修复数据的起始规格id
    private Integer maxGoodsSpecId = 64910000;// 修复数据的结束规格id

    /**
     * 每一条数据解析都会来调用
     *
     * @param context
     */
    @Override
    public void invoke(ImportDataItem data, AnalysisContext context) {
        totalCount.incrementAndGet();
        ModifyPriceInfoDTO modifyPriceInfoDTO = JSON.parseObject(data.getRow_data(), ModifyPriceInfoDTO.class);

        modifyPriceInfoDTO.setHighestPrice(modifyPriceInfoDTO.getHighestPrice().setScale(2));
        modifyPriceInfoDTO.setLowestPrice(modifyPriceInfoDTO.getLowestPrice().setScale(2));
        modifyPriceInfoDTO.setMarketPrice(modifyPriceInfoDTO.getMarketPrice().setScale(2));
        modifyPriceInfoDTO.setSupplyPrice(modifyPriceInfoDTO.getSupplyPrice().setScale(2));

        modifyPriceInfoDTO.setNewHighestPrice(modifyPriceInfoDTO.getNewLowestPrice().setScale(2));
        modifyPriceInfoDTO.setNewLowestPrice(modifyPriceInfoDTO.getNewLowestPrice().setScale(2));
        modifyPriceInfoDTO.setNewMarketPrice(modifyPriceInfoDTO.getNewMarketPrice().setScale(2));
        modifyPriceInfoDTO.setNewSupplyPrice(modifyPriceInfoDTO.getNewSupplyPrice().setScale(2));

        /**
         * 改价规格id列表，同时有下单的规格
         */
        List<GoodsSpec> goodsSpecIdList = GoodsSpecListener.getGoodsSpecIdList();
        for (GoodsSpec goodsSpec : goodsSpecIdList) {
            if (goodsSpec.getGoods_spec_id().equals(modifyPriceInfoDTO.getGoodsSpecId())) {

                /*if (checkImportPrice(modifyPriceInfoDTO)) {
                    LOGGER.info("goodsSpecId={} 导入商品改价，前后价格一致，无需处理", modifyPriceInfoDTO.getGoodsSpecId());
                } else {
                    LOGGER.info("goodsSpecId={} 导入商品改价，前后价格不一致，需要处理", modifyPriceInfoDTO.getGoodsSpecId());
                }*/
                dealCount.incrementAndGet();
                goodsSpec.setHighest_price(goodsSpec.getHighest_price().setScale(2));
                goodsSpec.setLowest_price(goodsSpec.getLowest_price().setScale(2));
                goodsSpec.setMarket_price(goodsSpec.getMarket_price().setScale(2));
                goodsSpec.setSupply_price(goodsSpec.getSupply_price().setScale(2));


                // 导入最新价格和DB价格比较
                if (checkNewPrice(modifyPriceInfoDTO, goodsSpec)) {
                    LOGGER.info("goodsSpecId={} 导入NewPrice和DB价格一致 [改价]NewSupplyPrice={}, [DB]Supply_price={}", modifyPriceInfoDTO.getGoodsSpecId(), modifyPriceInfoDTO.getNewSupplyPrice(), goodsSpec.getSupply_price());
                }

                // 修改前价格和DB价格比较
                if (checkPrice(modifyPriceInfoDTO, goodsSpec)) {
                    LOGGER.info("goodsSpecId={} 导入Price和DB价格一致 [改价]SupplyPrice={}, [DB]Supply_price={}", modifyPriceInfoDTO.getGoodsSpecId(), modifyPriceInfoDTO.getSupplyPrice(), goodsSpec.getSupply_price());
                    break;
                }

                minGoodsSpecId++;

                orderNotGoodsSnap(modifyPriceInfoDTO, goodsSpec);
            }
        }
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
    private void orderNotGoodsSnap(ModifyPriceInfoDTO modifyPriceInfoDTO, GoodsSpec goodsSpec) {

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

        Map<Integer, GoodsSpecSnapshot> map = GoodsSpecSnapshotListener.getGoodsSpecSnapshotMap();
        GoodsSpecSnapshot goodsSpecSnapshot = map.get(goodsSpec.getGoods_spec_id());
        if (null != goodsSpecSnapshot) {
            insertSql.append(goodsSpecSnapshot.getSupply_price()).append(",");
            insertSql.append(goodsSpecSnapshot.getLowest_price()).append(",");
        } else {
            insertSql.append(modifyPriceInfoDTO.getSupplyPrice()).append(",");
            insertSql.append(modifyPriceInfoDTO.getLowestPrice()).append(",");
        }
        insertSql.append(goodsSpec.getHighest_price()).append(",");
        insertSql.append(goodsSpec.getCost_price()).append(",");
        insertSql.append(goodsSpec.getMarket_price());
        insertSql.append(");");


        StringBuilder updateSql = new StringBuilder("update order_goods set ");
        updateSql.append(" goods_spec_id=").append(minGoodsSpecId);
        updateSql.append(" , remark=\"").append(goodsSpec.getGoods_spec_id()).append("\"");// 备份旧的规格id，用于追溯
        updateSql.append(" where goods_spec_snapshot_id=0 and goods_spec_id=").append(goodsSpec.getGoods_spec_id()).append(";");


        StringBuilder rockbackSql = new StringBuilder("update order_goods set ");
        rockbackSql.append(" goods_spec_id=").append(goodsSpec.getGoods_spec_id());
        rockbackSql.append(" , remark=\"").append(minGoodsSpecId).append("\"");
        rockbackSql.append(" where goods_spec_snapshot_id=0 and goods_spec_id=").append(minGoodsSpecId).append(";");

        LOGGER.info("goodsSpecId={} insert_sql={}", goodsSpec.getGoods_spec_id(), insertSql.toString());
        LOGGER.info("goodsSpecId={} update_sql={}", goodsSpec.getGoods_spec_id(), updateSql.toString());
        LOGGER.info("goodsSpecId={} rockbacksql={}", goodsSpec.getGoods_spec_id(), rockbackSql.toString());
    }

    private boolean checkNewPrice(ModifyPriceInfoDTO modifyPriceInfoDTO, GoodsSpec goodsSpec) {
        boolean rslt = true;
        if (!modifyPriceInfoDTO.getNewLowestPrice().equals(goodsSpec.getLowest_price())) {
            LOGGER.info("goodsSpecId={} 导入NewPrice和DB价格不一致 [改价]NewLowestPrice={}, [DB]Lowest_price={}", modifyPriceInfoDTO.getGoodsSpecId(), modifyPriceInfoDTO.getNewLowestPrice(), goodsSpec.getLowest_price());
            rslt = false;
        }
        if (!modifyPriceInfoDTO.getNewMarketPrice().equals(goodsSpec.getMarket_price())) {
            LOGGER.info("goodsSpecId={} 导入NewPrice和DB价格不一致 [改价]NewMarketPrice={}, [DB]Market_price={}", modifyPriceInfoDTO.getGoodsSpecId(), modifyPriceInfoDTO.getNewMarketPrice(), goodsSpec.getMarket_price());
            rslt = false;
        }
        if (!modifyPriceInfoDTO.getNewSupplyPrice().equals(goodsSpec.getSupply_price())) {
            LOGGER.info("goodsSpecId={} 导入NewPrice和DB价格不一致 [改价]NewSupplyPrice={}, [DB]Supply_price={}", modifyPriceInfoDTO.getGoodsSpecId(), modifyPriceInfoDTO.getNewSupplyPrice(), goodsSpec.getSupply_price());
            rslt = false;
        }
        return rslt;
    }


    private boolean checkPrice(ModifyPriceInfoDTO modifyPriceInfoDTO, GoodsSpec goodsSpec) {
        boolean rslt = true;
        if (!modifyPriceInfoDTO.getLowestPrice().equals(goodsSpec.getLowest_price())) {
            LOGGER.info("goodsSpecId={} 导入Price和DB价格不一致 [改价]LowestPrice={}, [DB]Lowest_price={}", modifyPriceInfoDTO.getGoodsSpecId(), modifyPriceInfoDTO.getLowestPrice(), goodsSpec.getLowest_price());
            rslt = false;
        }
        /*if (!modifyPriceInfoDTO.getMarketPrice().equals(goodsSpec.getMarket_price())) {
            LOGGER.info("goodsSpecId={} 导入Price和DB价格不一致 [改价]MarketPrice={}, [DB]Market_price={}", modifyPriceInfoDTO.getGoodsSpecId(), modifyPriceInfoDTO.getMarketPrice(), goodsSpec.getMarket_price());
            rslt = false;
        }*/
        if (!modifyPriceInfoDTO.getSupplyPrice().equals(goodsSpec.getSupply_price())) {
            LOGGER.info("goodsSpecId={} 导入Price和DB价格不一致 [改价]SupplyPrice={}, [DB]Supply_price={}", modifyPriceInfoDTO.getGoodsSpecId(), modifyPriceInfoDTO.getSupplyPrice(), goodsSpec.getSupply_price());
            rslt = false;
        }
        return rslt;
    }


    private boolean checkImportPrice(ModifyPriceInfoDTO modifyPriceInfoDTO) {
        boolean rslt = true;
        /*if (!modifyPriceInfoDTO.getNewHighestPrice().equals(modifyPriceInfoDTO.getHighestPrice())) {
            LOGGER.info("goodsSpecId={} [导入价格比较]价格不一致 [new]NewHighestPrice={}, [before]HighestPrice={}", modifyPriceInfoDTO.getGoodsSpecId(), modifyPriceInfoDTO.getNewHighestPrice(), modifyPriceInfoDTO.getHighestPrice());
            rslt = false;
        }*/
        if (!modifyPriceInfoDTO.getNewLowestPrice().equals(modifyPriceInfoDTO.getLowestPrice())) {
            LOGGER.info("goodsSpecId={} [导入价格比较]价格不一致 [new]NewLowestPrice={}, [before]LowestPrice={}", modifyPriceInfoDTO.getGoodsSpecId(), modifyPriceInfoDTO.getNewLowestPrice(), modifyPriceInfoDTO.getLowestPrice());
            rslt = false;
        }
        if (!modifyPriceInfoDTO.getNewMarketPrice().equals(modifyPriceInfoDTO.getMarketPrice())) {
            LOGGER.info("goodsSpecId={} [导入价格比较]价格不一致 [new]NewMarketPrice={}, [before]MarketPrice={}", modifyPriceInfoDTO.getGoodsSpecId(), modifyPriceInfoDTO.getNewMarketPrice(), modifyPriceInfoDTO.getMarketPrice());
            rslt = false;
        }
        if (!modifyPriceInfoDTO.getNewSupplyPrice().equals(modifyPriceInfoDTO.getSupplyPrice())) {
            LOGGER.info("goodsSpecId={} [导入价格比较]价格不一致 [new]NewSupplyPrice={}, [before]SupplyPrice={}", modifyPriceInfoDTO.getGoodsSpecId(), modifyPriceInfoDTO.getNewSupplyPrice(), modifyPriceInfoDTO.getSupplyPrice());
            rslt = false;
        }
        return rslt;
    }
}

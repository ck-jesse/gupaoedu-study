package com.coy.gupaoedu.study.spring.easyexcel.weeget.data;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author chenck
 * @date 2021/3/22 12:34
 */
public class GoodsSpecSnapshotListener extends AnalysisEventListener<GoodsSpecSnapshot> {

    /**
     * 改价规格id列表，同时有下单的规格
     */
    private static final List<GoodsSpecSnapshot> goodsSpecSnapshotList = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsSpecSnapshotListener.class);
    private AtomicLong totalCount = new AtomicLong();// 总数
    private AtomicLong dealCount = new AtomicLong();// 处理数量

    @Override
    public void invoke(GoodsSpecSnapshot goodsSpecSnapshot, AnalysisContext analysisContext) {
        totalCount.incrementAndGet();
        goodsSpecSnapshotList.add(goodsSpecSnapshot);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        LOGGER.info("所有数据解析完成！ totalCount={}", totalCount.get());
    }

    public static List<GoodsSpecSnapshot> getGoodsSpecSnapshotList() {
        return goodsSpecSnapshotList;
    }

    public static Map<Integer, GoodsSpecSnapshot> getGoodsSpecSnapshotMap() {
        // 等于是去重了
        Map<Integer, GoodsSpecSnapshot> map = goodsSpecSnapshotList.stream().collect(Collectors.toMap(goodsSpecSnapshot -> goodsSpecSnapshot.getGoods_spec_id(), goodsSpecSnapshot -> goodsSpecSnapshot));
        return map;
    }

    /**
     * 按规格id分组
     */
    public static Map<Integer, List<GoodsSpecSnapshot>> getGoodsSpecSnapshotListMap() {
        Map<Integer, List<GoodsSpecSnapshot>> map = goodsSpecSnapshotList.stream().collect(Collectors.groupingBy(goodsSpecSnapshot -> goodsSpecSnapshot.getGoods_spec_id()));
        return map;
    }

    /**
     * 过滤和区分哪些快照需要处理，哪些不需要处理
     */
    public static void dealSnapshot() {
        Map<Integer, List<GoodsSpecSnapshot>> map = GoodsSpecSnapshotListener.getGoodsSpecSnapshotListMap();

        // 规格有一条快照，无需处理
        // 规格有多条快照
        // 最后一条快照无需处理
        // 1到N之间的快照，需要比较，价格有变动则生成快照

        // 比较同一个规格的快照是否有修改过价格
        map.entrySet().forEach(entry -> {
            List<GoodsSpecSnapshot> goodsSpecSnapshotList = entry.getValue();
            goodsSpecSnapshotList.sort((o1, o2) -> (int) (o1.getAdd_time() - o2.getAdd_time()));// 排序

            GoodsSpecSnapshot goodsSpecSnapshot1 = goodsSpecSnapshotList.get(0);
            if (goodsSpecSnapshotList.size() == 1) {
                LOGGER.info("goodsSpecId={}, goodsSpecSnapshotId1={} 规格只有一条快照[无需处理]", entry.getKey(), goodsSpecSnapshot1.getGoods_spec_snapshot_id());
                return;
            }
            // 第一条
            // 从第二条开始比较，最后一条忽略（无需处理）
            for (int i = 1; i < goodsSpecSnapshotList.size(); i++) {
                if (goodsSpecSnapshotList.size() >= 3 && (goodsSpecSnapshotList.size() - 1) == i) {
                    break;
                }

                GoodsSpecSnapshot goodsSpecSnapshot2 = goodsSpecSnapshotList.get(i);
                boolean rslt = true;//
                /*if (!goodsSpecSnapshot1.getLowest_price().equals(goodsSpecSnapshot2.getLowest_price())) {
                    LOGGER.info("goodsSpecId={}, goodsSpecSnapshotId1={}, goodsSpecSnapshotId2={}  [快照比较]价格不一致 [快照1]Lowest_price={}, [快照2]Lowest_price={}", goodsSpecSnapshot2.getGoods_spec_id(),
                            goodsSpecSnapshot1.getGoods_spec_snapshot_id(), goodsSpecSnapshot2.getGoods_spec_snapshot_id(), goodsSpecSnapshot1.getLowest_price(), goodsSpecSnapshot2.getLowest_price());
                    rslt = false;
                }*/
                if (!goodsSpecSnapshot1.getSupply_price().equals(goodsSpecSnapshot2.getSupply_price())
                        || !goodsSpecSnapshot1.getLowest_price().equals(goodsSpecSnapshot2.getLowest_price())) {
                    LOGGER.info("goodsSpecId={}, goodsSpecSnapshotId1={}, goodsSpecSnapshotId2={}, size={} [快照比较]价格不一致 [快照1]Supply_price={}, [快照2]Supply_price={}, [快照1]Lowest_price={}, [快照2]Lowest_price={}", goodsSpecSnapshot2.getGoods_spec_id(),
                            goodsSpecSnapshot1.getGoods_spec_snapshot_id(), goodsSpecSnapshot2.getGoods_spec_snapshot_id(), goodsSpecSnapshotList.size(), goodsSpecSnapshot1.getSupply_price(), goodsSpecSnapshot2.getSupply_price(), goodsSpecSnapshot1.getLowest_price(), goodsSpecSnapshot2.getLowest_price());
                    rslt = false;
                }
                if (rslt) {
                    LOGGER.info("goodsSpecId={}, goodsSpecSnapshotId1={}, goodsSpecSnapshotId2={}, size={} [快照比较][无需处理]价格一致 [快照1]Supply_price={}, [快照2]Supply_price={}", goodsSpecSnapshot2.getGoods_spec_id(),
                            goodsSpecSnapshot1.getGoods_spec_snapshot_id(), goodsSpecSnapshot2.getGoods_spec_snapshot_id(), goodsSpecSnapshotList.size(), goodsSpecSnapshot1.getSupply_price(), goodsSpecSnapshot2.getSupply_price());
                }
                goodsSpecSnapshot1 = goodsSpecSnapshot2;
            }
        });
    }
}

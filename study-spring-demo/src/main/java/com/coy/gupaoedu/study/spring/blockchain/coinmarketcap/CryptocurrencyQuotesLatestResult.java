package com.coy.gupaoedu.study.spring.blockchain.coinmarketcap;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author chenck
 * @date 2022/1/3 11:58
 */
@Data
public class CryptocurrencyQuotesLatestResult extends CoinMarketCapBaseResult {

    /**
     * 数据对象
     */
    // private DataBean data;
    // 用Map接收多个数据（通用化处理，后续扩展性强一些）
    private Map<String, TokenBean> data;

    /**
     * 用Map接收多个数据（通用化处理，后续扩展性强一些）
     * 暂时无用
     */
    @Data
    public static class DataBean implements Serializable {

        /**
         * 代币集合
         */
        @JSONField(name = "BNB")
        private TokenBean bnb;

        @JSONField(name = "BTC")
        private TokenBean btc;
    }

    /**
     * 代币信息
     */
    @Data
    public static class TokenBean implements Serializable {

        private int id;
        private String name;
        private String symbol;
        private String slug;
        private int max_supply;
        private int circulating_supply;
        private int total_supply;
        private String last_updated;
        // private QuoteBean quote;
        // 用Map接收多个数据（通用化处理，后续扩展性强一些）
        private Map<String, ConvertBean> quote;

    }

    /**
     * 用Map接收多个数据（通用化处理，后续扩展性强一些）
     * 暂时无用
     */
    @Data
    public static class QuoteBean implements Serializable {

        @JSONField(name = "CNY")
        private ConvertBean cny;

        @JSONField(name = "USD")
        private ConvertBean usd;

    }

    /**
     * 币种相关信息
     */
    @Data
    public static class ConvertBean implements Serializable {
        private double price;
        private double volume_24h;
        private double volume_24h_reported;
        private double volume_7d;
        private double volume_7d_reported;
        private double volume_30d;
        private double volume_30d_reported;
        private double volume_change_24h;
        private double percent_change_1h;
        private double percent_change_24h;
        private double percent_change_7d;
        private double percent_change_30d;
        private double percent_change_60d;
        private double percent_change_90d;
        private double market_cap;
        private double market_cap_dominance;
        private double fully_diluted_market_cap;
        private double market_cap_by_total_supply;
        private String last_updated;
    }
}

package com.coy.gupaoedu.study.spring.blockchain.coinmarketcap;

import lombok.Data;

/**
 * // 方式1：通过id获取（id取自）
 * // url = url + "?id=1027&convert=USD";
 * <p>
 * // 方式2：通过slug获取
 * // url = url + "&slug=bitcoin";
 * <p>
 * // 方式2：通过symbol获取(免费版本一次只能获取一个币种数据)
 * //  url = url + "?symbol=BTC&convert=CNY";
 *
 * @author chenck
 * @date 2022/1/3 11:49
 */
@Data
public class CryptocurrencyQuotesLatestInput extends CoinMarketCapBaseInput<CryptocurrencyQuotesLatestResult> {

    public CryptocurrencyQuotesLatestInput() {
        super();
        super.setUrl("https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest");
    }

    /**
     * 代币的ID（从coinmarket市场获取）
     */
    public void setId(Integer id) {
        super.addUrlParam("id", id);// url 参数方式
    }

    /**
     * 币种(如：USD/CNY)
     * 免费版本API一次只能获取一个币种数据
     * 获取多个币种信息的参数格式，以逗号分隔，如： USD,CNY
     */
    public void setConvert(String convert) {
        super.addUrlParam("convert", convert);
    }

    /**
     * 如：bitcoin
     */
    public void setSlug(String slug) {
        super.addUrlParam("slug", slug);
    }

    /**
     * 代币代号（或简称，如：BNB/BTC）
     * 获取多各代币信息的参数格式，以逗号分隔，如： BNB,BTC
     */
    public void setSymbol(String symbol) {
        super.addUrlParam("symbol", symbol);
    }

    /**
     * 扩展字段
     * “num_market_pairs,cmc_rank,date_ added,tags,platform,max_supply,circulating_supply,total_supply,is_active,is_fiat”
     * （可选）指定要返回的补充数据字段的逗号分隔列表。
     * 通过num_market_pairs,cmc_rank,date_added,tags,platform,max_supply,circulating_supply,total_supply,market_cap_by_total_supply,volume_24h_reported,volume_7d,volume_7d_reported,volume_30d,volume_30d_reported,is_active,is_fiat以包含所有辅助字段。
     */
    public void setAux(String aux) {
        super.addUrlParam("aux", aux);
    }

}

package com.coy.gupaoedu.study.spring.blockchain.coinmarketcap;

import com.alibaba.fastjson.JSON;
import com.ck.platform.common.util.httpclient.HttpClientParam;
import com.ck.platform.common.util.httpclient.HttpClientUtil;
import com.ck.platform.common.util.httpclient.HttpMethod;
import com.ck.platform.common.util.httpclient.HttpResultDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.junit.Test;

/**
 * @author chenck
 * @date 2021/12/26 16:48
 */
public class CoinMarketCapAPI {

    // coinmarketcap 开发api-key
    private static String apiKey = "a5102955-c7c4-432a-8a80-eec521466441";

    // shadowfly 代理ip和port（国内无法访问，需通过海外代理）
    private static String proxyHost = "127.0.0.1";
    private static int proxyPort = 4780;


    /**
     * 全球指标
     */
    @Test
    public void globalLatest() {
        String url = "https://pro-api.coinmarketcap.com/v1/global-metrics/quotes/latest?convert=USD";
        getData(url);
    }

    @Test
    public void cryptocurrencyLatest() {
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest";
        // 方式1：通过id获取（id取自）
        // url = url + "?id=1027&convert=USD";

        // 方式2：通过slug获取
        // url = url + "&slug=bitcoin";

        // 方式2：通过symbol获取(免费版本一次只能获取一个币种数据)
        //  url = url + "?symbol=BTC&convert=CNY";
        url = url + "?symbol=BNB,BTC&convert=CNY&aux=max_supply,circulating_supply,total_supply,market_cap_by_total_supply,volume_24h_reported,volume_7d,volume_7d_reported,volume_30d,volume_30d_reported";
        getData(url);
    }

    @Test
    public void cryptocurrencyListingsLatest() {
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        url = url + "?start=1&limit=10&convert=USD";
        getData(url);
    }


    public static void getData(String url) {
        HttpClientParam reqParam = HttpClientParam.of(url)
                .setMethod(HttpMethod.GET.name());
        reqParam.setHeader("X-CMC_PRO_API_KEY", apiKey);

        RequestConfig.Builder builder = RequestConfig.custom();
        if (StringUtils.isNotBlank(proxyHost) && proxyPort > 0) {
            builder.setProxy(new HttpHost(proxyHost, Integer.valueOf(proxyPort)));
        }
        HttpResultDto httpResultDto = HttpClientUtil.invoke(reqParam, builder.build());
        System.out.println(httpResultDto.getBizDataObj());
    }

    public static void main(String[] args) {
        String json = "{\n" +
                "  \"status\": {\n" +
                "    \"timestamp\": \"2022-01-03T04:34:12.449Z\",\n" +
                "    \"error_code\": 0,\n" +
                "    \"error_message\": null,\n" +
                "    \"elapsed\": 28,\n" +
                "    \"credit_count\": 1,\n" +
                "    \"notice\": null\n" +
                "  },\n" +
                "  \"data\": {\n" +
                "    \"BNB\": {\n" +
                "      \"id\": 1839,\n" +
                "      \"name\": \"Binance Coin\",\n" +
                "      \"symbol\": \"BNB\",\n" +
                "      \"slug\": \"binance-coin\",\n" +
                "      \"max_supply\": 166801148,\n" +
                "      \"circulating_supply\": 166801148,\n" +
                "      \"total_supply\": 166801148,\n" +
                "      \"last_updated\": \"2022-01-03T04:33:00.000Z\",\n" +
                "      \"quote\": {\n" +
                "        \"CNY\": {\n" +
                "          \"price\": 3332.642527063336,\n" +
                "          \"volume_24h\": 9442901936.365965,\n" +
                "          \"volume_24h_reported\": 14158227976.359516,\n" +
                "          \"volume_7d\": 61486943317.87013,\n" +
                "          \"volume_7d_reported\": 66872482779.13411,\n" +
                "          \"volume_30d\": 18678553435981.94,\n" +
                "          \"volume_30d_reported\": 18686101360650.562,\n" +
                "          \"volume_change_24h\": -5.2161,\n" +
                "          \"percent_change_1h\": -0.46166801,\n" +
                "          \"percent_change_24h\": -0.10734666,\n" +
                "          \"percent_change_7d\": -5.70954754,\n" +
                "          \"percent_change_30d\": -8.7127435,\n" +
                "          \"percent_change_60d\": -6.17306223,\n" +
                "          \"percent_change_90d\": 22.79540974,\n" +
                "          \"market_cap\": 555888599387.7855,\n" +
                "          \"market_cap_dominance\": 3.9107,\n" +
                "          \"fully_diluted_market_cap\": 555888599387.786,\n" +
                "          \"market_cap_by_total_supply\": 555888599387.7855,\n" +
                "          \"last_updated\": \"2022-01-03T04:34:12.000Z\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"BTC\": {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"Bitcoin\",\n" +
                "      \"symbol\": \"BTC\",\n" +
                "      \"slug\": \"bitcoin\",\n" +
                "      \"max_supply\": 21000000,\n" +
                "      \"circulating_supply\": 18918512,\n" +
                "      \"total_supply\": 18918512,\n" +
                "      \"last_updated\": \"2022-01-03T04:33:00.000Z\",\n" +
                "      \"quote\": {\n" +
                "        \"CNY\": {\n" +
                "          \"price\": 298005.554390281,\n" +
                "          \"volume_24h\": 278905145905.5194,\n" +
                "          \"volume_24h_reported\": 604212680775.5135,\n" +
                "          \"volume_7d\": 1378055378367.9768,\n" +
                "          \"volume_7d_reported\": 1713580932732.708,\n" +
                "          \"volume_30d\": 7156135963814.027,\n" +
                "          \"volume_30d_reported\": 7491678433273.0205,\n" +
                "          \"volume_change_24h\": 79.3083,\n" +
                "          \"percent_change_1h\": -0.33734168,\n" +
                "          \"percent_change_24h\": -1.36113162,\n" +
                "          \"percent_change_7d\": -9.25509145,\n" +
                "          \"percent_change_30d\": -10.46124165,\n" +
                "          \"percent_change_60d\": -25.16622036,\n" +
                "          \"percent_change_90d\": -4.52804892,\n" +
                "          \"market_cap\": 5637821656799.184,\n" +
                "          \"market_cap_dominance\": 39.6624,\n" +
                "          \"fully_diluted_market_cap\": 6258116642195.922,\n" +
                "          \"market_cap_by_total_supply\": 5637821656799.184,\n" +
                "          \"last_updated\": \"2022-01-03T04:34:12.000Z\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        CryptocurrencyQuotesLatestResult resultDTO = JSON.parseObject(json, CryptocurrencyQuotesLatestResult.class);
        System.out.println(resultDTO.isSucc());
        System.out.println(JSON.toJSONString(resultDTO));
    }

}

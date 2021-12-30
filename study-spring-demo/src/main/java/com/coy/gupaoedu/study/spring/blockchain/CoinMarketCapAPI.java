package com.coy.gupaoedu.study.spring.blockchain;

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


    @Test
    public void globalLatest() {
        String url = "https://pro-api.coinmarketcap.com/v1/global-metrics/quotes/latest?convert=USD";
        getData(url);
    }

    @Test
    public void cryptocurrencyLatest() {
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest";
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

}

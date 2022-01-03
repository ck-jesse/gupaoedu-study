package com.coy.gupaoedu.study.spring.blockchain.coinmarketcap;

import com.ck.platform.common.util.httpclient.dto.HttpParamApiInput;
import lombok.Data;

/**
 * coinmarketcap 基础入参
 *
 * @author chenck
 * @date 2022/1/3 13:57
 */
@Data
public class CoinMarketCapBaseInput<T extends CoinMarketCapBaseResult> extends HttpParamApiInput<T> {

    // coinmarketcap 开发api-key
    private String apiKey;
}

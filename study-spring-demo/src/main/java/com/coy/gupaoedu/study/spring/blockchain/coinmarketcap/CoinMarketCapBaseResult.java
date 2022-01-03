package com.coy.gupaoedu.study.spring.blockchain.coinmarketcap;

import com.ck.platform.common.util.httpclient.dto.HttpApiResult;
import lombok.Data;

import java.io.Serializable;

/**
 * coinmarketcap 基础出参
 *
 * @author chenck
 * @date 2022/1/3 13:42
 */
@Data
public class CoinMarketCapBaseResult extends HttpApiResult implements Serializable {

    public static final int SUCC = 0;

    /**
     * 状态对象
     */
    private StatusBean status;

    @Data
    public static class StatusBean implements Serializable {
        private String timestamp;
        private int error_code;
        private Object error_message;
        private int elapsed;
        private int credit_count;
        private Object notice;
    }

    public boolean isSucc() {
        if (null == status) {
            return false;
        }
        if (status.getError_code() == SUCC) {
            return true;
        }
        return false;
    }
}

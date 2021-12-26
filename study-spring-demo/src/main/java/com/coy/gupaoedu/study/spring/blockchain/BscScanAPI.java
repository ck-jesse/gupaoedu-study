package com.coy.gupaoedu.study.spring.blockchain;

import com.ck.platform.common.util.httpclient.HttpClientUtil;
import com.ck.platform.common.util.httpclient.HttpResultDto;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author chenck
 * @date 2021/12/26 17:15
 */
public class BscScanAPI {

    // bscscan 开发api-key
    private static String appName = "FirstBSCKey";
    private static String apiKey = "27CW3HITQI5T9RMJ497Q5URTRKA2QPV985";

    public static void main(String[] args) throws URISyntaxException, IOException {

        String url = "https://api.bscscan.com/api?module=stats&action=tokensupply&contractaddress=0xe9e7cea3dedca5984780bafc599bd69add087d56&apikey=" + apiKey;

        get(url);
    }


    @Test
    public void tokensupply() throws IOException, URISyntaxException {
        String url = "https://api.bscscan.com/api?module=stats&action=tokensupply&contractaddress=0xe9e7cea3dedca5984780bafc599bd69add087d56&apikey=" + apiKey;
//        BscScanAPI.get(url);
        HttpResultDto httpResultDto = HttpClientUtil.doGet(url);
        System.out.println(httpResultDto.getBizDataObj());
    }

    // pro 版本接口
    @Test
    public void tokeninfo() throws IOException, URISyntaxException {
        String url = "https://api.bscscan.com/api?module=token&action=tokeninfo&contractaddress=0x0e09fabb73bd3ade0a17ecc321fd13a19e81ce82&apikey=" + apiKey;
        BscScanAPI.get(url);
    }

    // pro 版本接口
    @Test
    public void addresstokenbalance() throws IOException, URISyntaxException {
        String url = "https://api.bscscan.com/api?module=account&action=addresstokenbalance&address=0x99817ce62abf5b17f58e71071e590cf958e5a1bf&page=1&offset=100&apikey=" + apiKey;

        BscScanAPI.get(url);
    }


    private static String get(String url) throws URISyntaxException, IOException {
        URIBuilder query = new URIBuilder(url);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());

        request.setHeader(HttpHeaders.ACCEPT, "application/json");

        CloseableHttpResponse response = client.execute(request);
        String response_content = "";
        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            response_content = EntityUtils.toString(entity);
            System.out.println(response_content);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return response_content;
    }
}

package com.coy.gupaoedu.study.spring.m7;

import com.alibaba.fastjson.JSON;
import com.coy.gupaoedu.study.spring.easyexcel.weeget.JsonResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenck
 * @date 2023/3/16 20:11
 */
public class ApiDemo1 {
    private static final String account = "X00000020653";//替换为您的账户
    private static final String secret = "9033d920-c49d-11ed-b7e4-ed3a2d48e0f9";//替换为您的api密码
    private static final String host = "https://api.daxincc.cn";
    private static Log logger = LogFactory.getLog(ApiDemo1.class);

    public static void main(String[] args) {
        String time = getDateTime();
        String sig = md5(account + secret + time);
        //查询坐席状态接口
        String interfacePath = "/v20160818/webCall/webCall/";
        String url = host + interfacePath + account + "?sig=" + sig;
        String auth = base64(account + ":" + time);
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient client = builder.build();
        HttpPost post = new HttpPost(url);
        post.addHeader("Accept", "application/json");
        post.addHeader("Content-Type","application/json;charset=utf-8");
        post.addHeader("Authorization",auth);

        WebCallInput input = new WebCallInput();
        input.setServiceNo("0101111014");
        input.setExten("18601790917");
        input.setVariable("text:\"张先生，你好\"");
        String json = JSON.toJSONString(input);
        logger.info(json);
        //根据需要发送的数据做相应替换
        StringEntity requestEntity = new StringEntity(json,"UTF-8");
//        StringEntity requestEntity = new StringEntity("{\"Action\":\"Webcall\",\"Exten\":\"15828547326\",\"ServiceNo\":\"0101111014\",\"Variable\":\"phoneNum:13547522523\"} ","UTF-8");
        post.setEntity(requestEntity);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
            HttpEntity entity = response.getEntity();
            logger.info("the response is : " + EntityUtils.toString(entity,"utf8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String md5 (String text) {
        return DigestUtils.md5Hex(text).toUpperCase();
    }
    public static String base64 (String text) {
        byte[] b = text.getBytes();
        Base64 base64 = new Base64();
        b = base64.encode(b);
        String s = new String(b);
        return s;
    }
    public static String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }
}

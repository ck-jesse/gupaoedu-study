package com.coy.gupaoedu.study;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * https://square.github.io/okhttp/
 *
 * @author chenck
 * @date 2023/5/4 16:51
 */
public class OkHttpTest {

    public static final MediaType JSON = MediaType.get("application/json");
    OkHttpClient client = new OkHttpClient();

    @Test
    public void get() throws IOException {
        client.newBuilder().addInterceptor(null).build();
        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        }
    }

    @Test
    public void post() throws IOException {
        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .post(RequestBody.create(JSON, "json"))
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        }
    }
}

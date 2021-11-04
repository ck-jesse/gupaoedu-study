package com.coy.gupaoedu.study;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

public class ControllerTest {

    private static final String HOST = "http://127.0.0.1:8081";

    RestTemplate restTemplate = new RestTemplate();

    @Test
    public void brandGet() {
        String url = HOST + "/brandGet";

        String user = restTemplate.getForObject(url, String.class);

        System.out.println(user);
    }


}

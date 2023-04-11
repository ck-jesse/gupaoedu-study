package com.coy.gupaoedu.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OAuth2LoginApplication {

    /**
     * 修改hosts文件
     * 127.0.0.1 oauth2server
     * 127.0.0.1 oauth2login
     * <p>
     * 验证地址：http://oauth2login:8000
     */
    public static void main(String[] args) {
        SpringApplication.run(OAuth2LoginApplication.class);
    }
}

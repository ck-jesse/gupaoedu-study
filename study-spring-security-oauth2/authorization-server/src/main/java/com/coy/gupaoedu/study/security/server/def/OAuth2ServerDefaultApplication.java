package com.coy.gupaoedu.study.security.server.def;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Authorization Server 授权服务器，基于内存的默认模式
 * <p>
 */
@SpringBootApplication
public class OAuth2ServerDefaultApplication {

    /**
     * 获取授权码的URL
     * <p>
     * http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=message.read&redirect_uri=http://127.0.0.1:9000/authorized
     * http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=message.read&redirect_uri=http://oauth2login:8000/authorized
     */
    public static void main(String[] args) {
        SpringApplication.run(OAuth2ServerDefaultApplication.class, args);
    }
}

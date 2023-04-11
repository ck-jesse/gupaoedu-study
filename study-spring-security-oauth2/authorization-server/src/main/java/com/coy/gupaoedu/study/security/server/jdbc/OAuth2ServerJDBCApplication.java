package com.coy.gupaoedu.study.security.server.jdbc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Authorization Server 授权服务器，基于DB的存储配置
 */
@SpringBootApplication
@RestController
public class OAuth2ServerJDBCApplication {

    /**
     * 获取授权码的URL
     * <p>
     * scope=message.read
     * http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=message.read&redirect_uri=http://127.0.0.1:9000/authorized
     * <p>
     * scope=openid
     * http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=openid&redirect_uri=http://127.0.0.1:9000/authorized
     * <p>
     * scope=client.create
     * http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=messaging-client&scope=client.create&redirect_uri=http://127.0.0.1:9000/authorized
     */
    public static void main(String[] args) {
        SpringApplication.run(OAuth2ServerJDBCApplication.class, args);
    }
}

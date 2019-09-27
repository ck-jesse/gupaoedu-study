package com.coy.gupaoedu.study.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@Configuration
public class RedisBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(RedisBootstrap.class, args);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration();
        serverConfig.setPort(6379);
//        serverConfig.setHostName("localhost");
        //
        serverConfig.setHostName("172.100.37.90");
        serverConfig.setPassword("Vzvwg35O");
        return new LettuceConnectionFactory(serverConfig);
    }
}
package com.coy.gupaoedu.study.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 配置中心 服务端
 *
 * @author chenck
 * @date 2019/9/4 10:56
 */
@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class ConfigServerApplication {

    /**
     * 远程仓库https://github.com/forezp/SpringcloudConfig/ 中有个文件config-client-dev.properties文件
     * <p>
     * 访问 http://localhost:8888/foo/dev
     * 返回 {"name":"foo","profiles":["dev"],"label":null,"version":"0fc8081c507d694b27967e9074127b373d196431",
     * "state":null,"propertySources":[]}
     * 证明程序可以从远程配置中心获取配置信息。
     * <p>
     * http请求地址和资源文件映射如下:
     * /{application}/{profile}[/{label}]
     * /{application}-{profile}.yml
     * /{label}/{application}-{profile}.yml
     * /{application}-{profile}.properties
     * /{label}/{application}-{profile}.properties
     *
     * 如： http://localhost:8888/config-client/dev
     * {"name":"config-client","profiles":["dev"],"label":null,"version":"0fc8081c507d694b27967e9074127b373d196431","state":null,"propertySources":[{"name":"https://github.com/forezp/SpringcloudConfig//respo/config-client-dev.properties","source":{"foo":"foo version 21","democonfigclient.message":"hello spring io"}}]}
     */
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}

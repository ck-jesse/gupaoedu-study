

# Spring Boot 多配置文件

> study-spring-cloud-gateway 工程中的配置文件，可找到下面的影子。

## 多环境配置文件

Spring Boot中多环境配置文件名需要满足application-{profile}.properties的格式，其中{profile}对应你的环境标识，比如：

> application.properties
>
> application-dev.properties- 开发环境
>
> application-test.properties- 测试环境
>
> application-prod.properties- 生产环境
>
> 注：可以是properties文件，也可以是yml文件。
>
> 
>
> 先加载 application.properties ，然后在加载指定的 profiles 文件，
>
> 如果 application.properties 和 profiles 文件中有相同的配置项，则以 profiles 文件中的配置项为准。
>
> application.properties 文件中写通用的配置项，profiles 文件中写特定环境的配置项

### 环境激活

#### 使用配置文件激活

在 application.properties  文件中通过 spring.profiles.active 属性来设置需要激活的 {profile}。

- 激活单个profile

  ```properties
  # 表示加载 dev 环境配置
  spring.profiles.active=dev
  ```

- 激活多个profile

  ```properties
  # 表示加载 dev和test 环境配置文件
  # 注意：当 dev和test 环境配置文件中存在重复的元素配置时，最后一个profile的元素会生效
  spring.profiles.active=dev,test
  ```

  

#### 使用命令行激活

> 测试不同配置的加载:
>
> - 执行java -jar xxx.jar，可以观察到服务端口被设置为8082，也就是默认的开发环境（dev）
> - 执行java -jar xxx.jar –spring.profiles.active=test，可以观察到服务端口被设置为8083，也就是测试环境的配置（test）





#### 使用编程方式激活

SpringApplication的入口点也提供了一个用于设置额外配置的Java API（比如，在那些通过spring.profiles.active属性生效的配置之上）：

```java
@SpringBootApplication
@EnableDiscoveryClient // 开启服务发现功能
public class SpringCloudGatewayApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringCloudGatewayApplication.class, args);
        // TODO 好像不起作用
        applicationContext.getEnvironment().setActiveProfiles("after_route");
    }
}

```





#### 基于Java配置的profiles

Spring Profiles提供了一种隔离应用程序配置的方式，并让这些配置只能在特定的环境下生效。任何@Component或@Configuration都能被@Profile标记

```java
@Configuration
@Profile("production")
public class ProductionConfiguration {
// ...
}
```



## 多配置文件 ---

在` application.yml` 中通过三个横线 `---` 来再创建一个配置文件，

在此配置文件中通过 `spring.profiles` 来配置文件名，和 `spring.profiles.active` 保持一致，

```yaml
server:
  port: 8081
spring:
  application:
    name: spring-cloud-gateway
  profiles:
    active: header_route

---
spring:
  profiles: header_route
  cloud:
    gateway:
     # routes 是一个集合，配置多个元素时，使用单个横线 "-" 来标记一个元素
      routes:
        - id: header_route
          uri: http://httpbin.org:80/get
          predicates:
            - Header=X-Request-Id, \d+

---
spring:
  profiles: cookie_route
  cloud:
    gateway:
      routes:
        - id: cookie_route
          uri: http://httpbin.org:80/get
          predicates:
            - Cookie=name, forezp
```




















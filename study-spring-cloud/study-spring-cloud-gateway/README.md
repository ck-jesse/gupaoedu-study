# Spring Cloud Gateway 网关简介

> 参考 方志朋 的博客
> https://www.fangzhipeng.com/springcloud/2018/11/06/sc-f-gateway1.html



> Spring Cloud Gateway是Spring Cloud官方推出的第二代网关框架，取代Zuul网关，在功能上要比Zuul更加的强大，性能也更好。
> 网关作为一个系统的流量入口，在微服务系统中有着非常作用，网关常见的功能有路由转发、权限校验、限流控制等作用。

- 网关的作用
  - 协议转换，路由转发
  - 权限校验
  - 缓存
  - 流量聚合，对流量进行监控，日志输出
  - 作为整个系统的前端工程，对流量进行控制，有限流作用
  - 作为系统的前端边界，外部流量只能通过网关才能访问系统

![img](https://raw.githubusercontent.com/spring-cloud/spring-cloud-gateway/master/docs/src/main/asciidoc/images/spring_cloud_gateway_diagram.png)



# Predicate 简介

> Predicate来自于java8的接口。Predicate 接受一个输入参数，返回一个布尔值结果。该接口包含多种默认方法来将Predicate组合成其他复杂的逻辑（比如：与，或，非）。可以用于接口请求参数校验、判断新老数据是否有变化需要进行更新操作。add--与、or--或、negate--非。





# Filter 简介



## 自定义过滤器 - `GatewayFilter`

- 打印请求时间的自定义过滤器 - RequestTimeGatewayFilter -> GatewayFilter
- 需要将该自定义过滤器注册到网关Route中才能生效，所以需要结合  RouteLocatorBuilder 来创建路由，和添加各种 predicates和filters。

```java
@Bean
public RouteLocator myRoutes(RouteLocatorBuilder builder) {
	// 作为转发请求的域名部分
	String httpBinUrl = "http://httpbin.org:80";

	// 通过 RouteLocatorBuilder 创建路由，同时可添加各种 predicates和filters
	// predicates断言的意思，顾名思义就是根据具体的请求规则，去匹配具体的route去处理，
	// filters是各种过滤器，用来对请求做各种判断和修改。
	return builder.routes()
			// 定义一个 router（函数式编程）
			// 此处定义了一个 predicate 断言配置，使请求与该 router 匹配，该router配置实现功能：让“/get”请求都转发到“http://httpbin.org/get”
			.route(p -> p
					.path("/get")
					// 添加一个 filter ，往请求头里面添加一个Header(key=Hello,value=World)
					.filters(f -> f.filter(new RequestTimeGatewayFilter()).addRequestHeader("Hello", "World"))
					// 路由的目标url，实际为重定向地址
					.uri(httpBinUrl))
			// 定义一个 router（普通编程） 用于与上面比较
			.route(new Function<PredicateSpec, Route.AsyncBuilder>(){
				@Override
				public Route.AsyncBuilder apply(PredicateSpec predicateSpec) {
					return predicateSpec.path("/ip")
							.filters(new Function<GatewayFilterSpec, UriSpec>(){
								@Override
								public UriSpec apply(GatewayFilterSpec gatewayFilterSpec) {
									// 自定义filter过滤器
									return gatewayFilterSpec.filter(new RequestTimeGatewayFilter()).addRequestHeader("Hello", "World");
								}
							})
							.uri(httpBinUrl);
				}
			})
			.build();
}
```



## 自定义过滤器工厂 - `GatewayFilterFactory`

- 打印请求时间的自定义过滤器工厂 - RequestTimeGatewayFilterFactory -> AbstractGatewayFilterFactory ->  GatewayFilterFactory
- 可在配置文件中配置过滤器，实现一个自定义过滤器工厂打印日志等功能

```properties
spring:
  cloud:
    gateway:
     # routes 是一个集合，配置多个元素时，使用单个横线 "-" 来标记一个元素
      routes:
        - id: after_route
          uri: http://httpbin.org:80/get
          predicates:
            - After=2017-11-20T17:42:47.789-07:00[America/Denver]
          # RequestTime是一个自定义过滤器工厂的简写，对应到RequestTimeGatewayFilterFactory
          filters:
            - RequestTime=true
  profiles: after_route
```





## 全局过滤器 - GlobalFilter

根据作用范围划分为GatewayFilter和GlobalFilter，二者区别如下：

- GatewayFilter : 需要通过spring.cloud.routes.filters 配置在具体路由下，只作用在当前路由上或通过spring.cloud.default-filters配置在全局，作用在所有路由上

- GlobalFilter : 全局过滤器，不需要在配置文件中配置，作用在所有的路由上，最终通过GatewayFilterAdapter包装成GatewayFilterChain可识别的过滤器，它为请求业务以及路由的URI转换为真实业务服务的请求地址的核心过滤器，不需要配置，系统初始化时加载，并作用在每个路由上。
- Spring Cloud Gateway框架内置的GlobalFilter如下：

![img](https://raw.githubusercontent.com/guofazhan/image/master/GlobalFilter.png)



## 自定义全局过滤器

当Spring Cloud Gateway内置的GlobalFilter无法满足业务上的需求时，则需要编写满足自己需求的GlobalFilter。



# 限流篇

一般限流都是在网关这一层做，比如Nginx、Openresty、kong、zuul、Spring Cloud Gateway等；也可以在应用层通过Aop这种方式去做限流。

## 常见的限流算法

- 计数器算法

  >计数器算法采用计数器实现限流有点简单粗暴，一般我们会限制一秒钟的能够通过的请求数，比如限流qps为100，算法的实现思路就是从第一个请求进来开始计时，在接下去的1s内，每来一个请求，就把计数加1，如果累加的数字达到了100，那么后续的请求就会被全部拒绝。等到1s结束后，把计数恢复成0，重新开始计数。
  >
  >具体的实现可以是这样的：对于每次服务调用，可以通过AtomicLong#incrementAndGet()方法来给计数器加1并返回最新值，通过这个最新值和阈值进行比较。这种实现方式，相信大家都知道有一个弊端：如果我在单位时间1s内的前10ms，已经通过了100个请求，那后面的990ms，只能眼巴巴的把请求拒绝，我们把这种现象称为“突刺现象”
  >
  >

- 漏桶算法

  >为了消除”突刺现象”，可以采用漏桶算法实现限流，漏桶算法这个名字就很形象，算法内部有一个容器，类似生活用到的漏斗，当请求进来时，相当于水倒入漏斗，然后从下端小口慢慢匀速的流出。不管上面流量多大，下面流出的速度始终保持不变。不管服务调用方多么不稳定，通过漏桶算法进行限流，每10毫秒处理一次请求。因为处理的速度是固定的，请求进来的速度是未知的，可能突然进来很多请求，没来得及处理的请求就先放在桶里，既然是个桶，肯定是有容量上限，如果桶满了，那么新进来的请求就丢弃。
  >
  >![img](https://forezp.obs.myhuaweicloud.com/img/jianshu/6615987-ae8673111579969f.png)
  >
  >在算法实现方面，可以准备一个队列，用来保存请求，另外通过一个线程池（ScheduledExecutorService）来定期从队列中获取请求并执行，可以一次性获取多个并发执行。
  >
  >这种算法，在使用过后也存在弊端：无法应对短时间的突发流量。
  >
  >

- 令牌桶算法

  >令牌桶算法是对漏桶算法的一种改进，桶算法能够限制请求调用的速率，而令牌桶算法能够在限制调用的平均速率的同时还允许一定程度的突发调用。在令牌桶算法中，存在一个桶，用来存放固定数量的令牌。算法中存在一种机制，以一定的速率往桶中放令牌。每次请求调用需要先获取令牌，只有拿到令牌，才有机会继续执行，否则选择选择等待可用的令牌、或者直接拒绝。放令牌这个动作是持续不断的进行，如果桶中令牌数达到上限，就丢弃令牌，所以就存在这种情况，桶中一直有大量的可用令牌，这时进来的请求就可以直接拿到令牌执行，比如设置qps为100，那么限流器初始化完成一秒后，桶中就已经有100个令牌了，这时服务还没完全启动好，等启动完成对外提供服务时，该限流器可以抵挡瞬时的100个请求。所以，只有桶中没有令牌时，请求才会进行等待，最后相当于以一定的速率执行。
  >
  >![img](https://forezp.obs.myhuaweicloud.com/img/jianshu/6615987-348db0c2328ddee7.png)
  >
  >实现思路：可以准备一个队列，用来保存令牌，另外通过一个线程池定期生成令牌放到队列中，每来一个请求，就从队列中获取一个令牌，并继续执行。
  >
  >



## Spring Cloud Gateway限流



- 基于Redis来进行限流

RequestRateLimiterGatewayFilterFactory - 请求限流过滤器工厂

RateLimiter -> RedisRateLimiter  - redis限流器

KeyResolver -> 自定义限流的键的解析器

```properties
spring:
  cloud:
    gateway:
      routes:
        - id: after_route
          uri: http://httpbin.org:80/get
          predicates:
            - After=2017-11-20T17:42:47.789-07:00[America/Denver]
          filters:
            - name: RequestRateLimiter
              args:
                # 用于限流的键的解析器的 Bean 对象的名字。它使用 SpEL 表达式根据#{@beanName}从 Spring 容器中获取 Bean 对象。
                key-resolver: '#{@hostAddrKeyResolver}'
                # 当令牌不足时，返回 HttpStatus.TOO_MANY_REQUESTS(429, "Too Many Requests")
                # 令牌桶每秒填充平均速率
                redis-rate-limiter.replenishRate: 1
                # 令牌桶总容量
                redis-rate-limiter.burstCapacity: 1
```





# Spring Cloud Gateway之服务注册与发现

## 基于 Spring Cloud 框架 - Eureka 注册中心

- 开启服务发现功能

```java
// 在spring-boot应用的启动类上标记
@EnableDiscoveryClient // 开启服务发现功能
```

- 引入 eureka 依赖

```xml
<!-- eureka client -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

- application.yml 配置

  > 指定程序的启动端口，注册中心地址、gateway的配置等信息

  ```properties
  server:
    port: 8081
  spring:
    application:
      name: spring-cloud-gateway
    # 网关配置
    cloud:
      gateway:
        discovery:
          locator:
            # true 表明gateway开启服务注册和发现的功能
            # 并且spring cloud gateway自动根据服务发现为每一个服务创建了一个router，这个router将以服务名开头的请求路径转发到对应的服务。
            enabled: true
            # true 将请求路径上的服务名配置为小写（因为服务注册的时候，向注册中心注册时将服务名转成大写的了）
            # 比如以/service-hi/*的请求路径被路由转发到服务名为service-hi的服务上。
            lower-case-service-id: true
  # 注册中心地址 - spring cloud gateway之服务注册与发现 - 配合服务注册中心进行路由转发
  eureka:
    client:
      service-url:
        defaultZone: http://localhost:8765/eureka/
  ```

- 启动应用

  > - study-eureka-server 注册中心
  > - study-eureka-provider 服务提供者，服务名为 eureka-provider-service
  > - study-eureka-feign 服务提供者和服务消费者，服务名为 eureka-consumer-service
  > - study-spring-cloud-gateway，服务名为 spring-cloud-gateway

  

  > http://localhost:8081/eureka-consumer-service/getServices
  >
  > http://localhost:8081/eureka-provider-service/home
  >
  > http://localhost:8081/eureka-consumer-service/home
  >
  > 

## 基于 Dubbo 框架 - Nacos 注册中心



> - Spring Cloud Alibaba 官网
>   -  https://spring.io/projects/spring-cloud-alibaba
>
> - spring-cloud-alibaba 样例
>   - https://github.com/alibaba/spring-cloud-alibaba/tree/master/spring-cloud-alibaba-examples/spring-cloud-alibaba-dubbo-examples

引入nacos依赖

```xml
<!-- Spring Cloud Nacos Service Discovery -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>

```







# Spring Cloud Gateway 的配置加载初始化解读

> 1、基于spring-boot的配置加载实现网关路由Route初始化
>
> org.springframework.cloud.gateway.filter.WeightCalculatorWebFilter#onApplicationEvent
>
> 2、基于自动属性装配来初始化
>
> org.springframework.cloud.gateway.config.GatewayAutoConfiguration
>
> 3、路由定义 - 含加载predicate和route	
>
> org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator#initFactories
>
> org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator#getRoutes
>
> getRoutes() -> convertToRoute() -> combinePredicates() -> getFilters() -> loadGatewayFilters ->
>
> org.springframework.cloud.gateway.support.ShortcutConfigurable#shortcutType
>
> org.springframework.cloud.gateway.support.ShortcutConfigurable.ShortcutType#normalize 来解析参数与其对应的值，
>
> org.springframework.cloud.gateway.support.ConfigurationUtils#bind - 将参数设置到RequestTimeGatewayFilterFactory.Config类的属性上





spring 属性绑定源码解析

加载网关过滤器（含工厂）

org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator#loadGatewayFilters

根据过滤器配置来绑定参数到对应的Config对象上

org.springframework.cloud.gateway.support.ConfigurationUtils#bind

org.springframework.boot.context.properties.bind.Binder#bind(org.springframework.boot.context.properties.source.ConfigurationPropertyName, org.springframework.boot.context.properties.bind.Bindable<T>, org.springframework.boot.context.properties.bind.BindHandler, org.springframework.boot.context.properties.bind.Binder.Context, boolean)

org.springframework.boot.context.properties.bind.Binder#bindObject

org.springframework.boot.context.properties.bind.Binder#bindBean

org.springframework.boot.context.properties.bind.Binder#bind(org.springframework.boot.context.properties.source.ConfigurationPropertyName, org.springframework.boot.context.properties.bind.Bindable<T>, org.springframework.boot.context.properties.bind.BindHandler, org.springframework.boot.context.properties.bind.Binder.Context, boolean)

绑定bean属性

org.springframework.boot.context.properties.bind.JavaBeanBinder#bind(org.springframework.boot.context.properties.source.ConfigurationPropertyName, org.springframework.boot.context.properties.bind.Bindable<T>, org.springframework.boot.context.properties.bind.Binder.Context, org.springframework.boot.context.properties.bind.BeanPropertyBinder)

具有已知的可绑定属性

org.springframework.boot.context.properties.bind.JavaBeanBinder#hasKnownBindableProperties

获取目标对象（含目标对象类型，属性集合），解析目标对象的属性和方法

org.springframework.boot.context.properties.bind.JavaBeanBinder.Bean#get(target, hasKnownBindableProperties);

绑定属性

org.springframework.boot.context.properties.bind.JavaBeanBinder#bind(org.springframework.boot.context.properties.bind.BeanPropertyBinder, org.springframework.boot.context.properties.bind.JavaBeanBinder.Bean<T>, org.springframework.boot.context.properties.bind.JavaBeanBinder.BeanSupplier<T>)






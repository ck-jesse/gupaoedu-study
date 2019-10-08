# Spring Cloud Gateway 网关

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



- Predicate 简介

> Predicate来自于java8的接口。Predicate 接受一个输入参数，返回一个布尔值结果。该接口包含多种默认方法来将Predicate组合成其他复杂的逻辑（比如：与，或，非）。可以用于接口请求参数校验、判断新老数据是否有变化需要进行更新操作。add--与、or--或、negate--非。





- Filter 简介







- Spring Cloud Gateway 的配置加载初始化解读

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










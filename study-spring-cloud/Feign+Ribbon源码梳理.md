20190924

## SpringBoot + SpringCloud 启动时 Feign 的加载流程

> 1、 启动类上标记 `@EnableFeignClients`  注解 - 开启 Feign 功能
>
> 2、分析  `@Import(FeignClientsRegistrar.class)`  - 注册 Feign 相关 BeanDefinition 到容器
>
> - `@Import`  的作用
>   - 声明一个bean，注册到容器中
>   - 导入@Configuration注解的配置类
>   - 导入ImportSelector的实现类
>   - 导入ImportBeanDefinitionRegistrar的实现类
>
> 3、分析 FeignClientsRegistrar
>
> - 实现 BeanDefinition 注册方法 `org.springframework.cloud.openfeign.FeignClientsRegistrar#registerBeanDefinitions` 
>
> - 获取标记了 @EnableFeignClients 的类的全路径，并作为 basePackages
>
> - 扫描 basePackages 下定义了 `@FeignClient` 注解的类的 BeanDefinition 对象集合
>
>   - 通过 ClassPathScanningCandidateComponentProvider..addIncludeFilter(new AnnotationTypeFilter(FeignClient.class)) ，只扫描出 `@FeignClient`的类
>
> - 遍历定义了 `@FeignClient` 注解的 BeanDefinition 对象集合
>
>   - 循环构建beanClass 为 `FeignClientFactoryBean` 的 BeanDefinition 对象 ，并将其注册到spring容器中4、
>
> - FeignClientFactoryBean 实现了 `org.springframework.beans.factory.FactoryBean` 
>
> - 通过 FeignClientFactoryBean .getObject() 获取远程通信的代理对象
>
>   
>
> 4、分析 FeignClientFactoryBean 
>
> - 前提：在代码中注入 `@FeignClient` 标记的API 接口，那么在启动时会自动进行注入，注入的api的bean是通过FeignClientFactoryBean .getObject() 来生成的，所以下面来分析该方法的实现原理
> - 定义了 FeignContext 对象，该对象用来管理名称不同的 AnnotationConfigApplicationContext 
>   - 注意：设计为多个ApplicationContext的目的是区分不同的微服务，也就是每一个微服务应用对应一个自己的ApplicationContext
> - 创建一个 Feign.Builder 对象 - 用来构建 Feign 对象
>   - 其中 Feign 对象 包含 feign.Client 对象(负载均衡) 和 feign.Target 对象(feign.Target.HardCodedTarget)
> - 获取 `feign.Client` 的实现类 - 获取 负载均衡 对象，如LoadBalancerFeignClient
>   - 注意：其中有一个逻辑判断 `@FeignClient` 注解是否有定义url属性
>     - url 属性为空，则构建一个url，其中IP:Port部分为 name 属性的值，表示走负载均衡
>     - url 属性不为空，则获取 `feign.Client` 的实现类的对象（如LoadBalancerFeignClient），当该client对象不为空时，则设置到 Feign.Builder中
>   - 
> - 定义 feign.Target.HardCodedTarget 对象
> - 通过 Feign.Builder.target(HardCodedTarget ) 创建远程代理对象
>   - 通过 JDK 动态代理实现
>   - java.lang.reflect.Proxy#newProxyInstance - 创建api接口的代理对象
>   - java.lang.reflect.InvocationHandler -  实际执行类为`feign.ReflectiveFeign.FeignInvocationHandler`
> - 到此，加载流程已完毕，后续进行api调用时，跟踪 `feign.ReflectiveFeign.FeignInvocationHandler#invoke` 即可
> - 
>
> 
>
> 
>
> 5、
>
> 6、
>
> 





## Feign 调用服务流程梳理

> 从接口代理对象 FeignInvocationHandler#invoke 为入口
>
> feign.SynchronousMethodHandler#executeAndDecode
>
> org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient#execute
>
> 基于负载均衡来执行
>
> com.netflix.client.AbstractLoadBalancerAwareClient#executeWithLoadBalancer
>
> 选择服务
>
> com.netflix.loadbalancer.reactive.LoadBalancerCommand#selectServer
>
> 从负载均衡器中选择一个服务
>
> com.netflix.loadbalancer.LoadBalancerContext#getServerFromLoadBalancer
>
> 给予负载均衡器选择服务（负载均衡）
>
> com.netflix.loadbalancer.ZoneAwareLoadBalancer#chooseServer
>
> 





## 服务发现梳理

>  启动类上标记 `@EnableDiscoveryClient` 
>
> org.springframework.cloud.client.discovery.DiscoveryClient
>
> org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient
>
> org.springframework.cloud.alibaba.nacos.discovery.NacosDiscoveryClient
>
> 
>
> 自动更新服务列表
>
> com.netflix.loadbalancer.PollingServerListUpdater#start
>
> com.netflix.loadbalancer.ServerListUpdater.UpdateAction
>
> 更新服务列表
>
> com.netflix.loadbalancer.ZoneAwareLoadBalancer#updateListOfServers
>
> 选择服务（负载均衡）
>
> com.netflix.loadbalancer.ZoneAwareLoadBalancer#chooseServer



> com.netflix.loadbalancer.DynamicServerListLoadBalancer#updateListOfServers
>
> 
>
> 获取更新的服务列表
>
> org.springframework.cloud.netflix.ribbon.eureka.DomainExtractingServerList#getUpdatedListOfServers
>
> 
>
> com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList#getUpdatedListOfServers
>
> 该类中有一个 Provider<EurekaClient> eurekaClientProvider 属性，
>
> 可从中获取到 EurekaClient (extends  DiscoveryClient )对象
>
> org.springframework.cloud.netflix.eureka.CloudEurekaClient - spring cloud对服务发现的扩展实现
>
> com.netflix.discovery.EurekaClient#getInstancesByVipAddress - 服务发现的具体实现类
>
> com.netflix.discovery.DiscoveryClient#getInstancesByVipAddress - 这是服务发现类
>
> 通过下面方法获取服务实例信息
>
> com.netflix.discovery.EurekaClient#getInstancesByVipAddress(java.lang.String, boolean, java.lang.String)





openapi-gateway


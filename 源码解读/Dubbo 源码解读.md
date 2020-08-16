# Dubbo SPI解读 - ExtensionLoader

- 核心类
  - ExtensionLoader


## 1、加载扩展点时的特性：
- 1）自动包装扩展点

> 自动包装扩展点的 Wrapper 类。`ExtensionLoader` 在加载扩展点时，如果加载到的扩展点有拷贝构造函数，则判定为扩展点 Wrapper 类。
>
> Wrapper 类同样实现了扩展点接口，但是 Wrapper 不是扩展点的真正实现。它的用途主要是用于从 `ExtensionLoader` 返回扩展点时，包装在真正的扩展点实现外。即从 `ExtensionLoader` 中返回的实际上是 Wrapper 类的实例，Wrapper 持有了实际的扩展点实现类。
>
> 通过 Wrapper 类可以把所有扩展点公共逻辑移至 Wrapper 中。新加的 Wrapper 在所有的扩展点上添加了逻辑，有些类似 AOP，即 Wrapper 代理了扩展点。

- 2）自动装配扩展点

> 加载扩展点时，自动注入依赖的扩展点。加载扩展点时，扩展点实现类的成员如果为其它扩展点类型，`ExtensionLoader` 在会自动注入依赖的扩展点。`ExtensionLoader` 通过扫描扩展点实现类的所有 setter 方法来判定其成员。即 `ExtensionLoader` 会执行扩展点的拼装操作。

## 2、三种扩展点
- 1）普通扩展点
> org.apache.dubbo.common.extension.ExtensionLoader.getExtension(name)
> 
> 根据name获取扩展点。

- 2）自适应扩展点 
> org.apache.dubbo.common.extension.ExtensionLoader.getAdaptiveExtension
>
> Dubbo 使用 URL 对象（包含了Key-Value）传递配置信息。扩展点方法调用会有URL参数（或是参数有URL成员），可根据URL参数动态来获取扩展点的key，然后扩展点方法执行时才决定调用哪一个扩展点实现。
>
> 在 Dubbo 的 `ExtensionLoader` 的扩展点类对应的 `Adaptive` 实现是在加载扩展点里动态生成。指定提取的 URL 的 Key 通过 `@Adaptive` 注解在接口方法上提供。
>
> @Adaptive修饰在方法级别，返回一个动态代理类。
> 
> @Adaptive修饰在类级别，直接返回修饰的类。

- 3）自动激活扩展点

> org.apache.dubbo.common.extension.ExtensionLoader.getActivateExtension 
>
> 对于集合类扩展点，比如：`Filter`, `InvokerListener`, `ExportListener`, `TelnetHandler`, `StatusChecker` 等，可以同时加载多个实现，此时，可以用自动激活来简化配置。
>
> 自动激活扩展点，会激活多个扩展点实现，支持order排序，支持group过滤，这一个扩展点集合组成一条链路，变相的实现责任链模式。
>
> 思考：openapi-gateway 中对于自定义扩展点GatewayRouter就是属于自动激活扩展点，该自定义Route被插入到了Route链中来进行路由。
>

# 服务发布流程解读 - ServiceConfig
服务发布入口
> org.apache.dubbo.config.ServiceConfig.doExport
> org.apache.dubbo.config.ServiceConfig.doExportUrls

获取注册中心列表-支持多注册中心
> org.apache.dubbo.config.AbstractInterfaceConfig.loadRegistries

遍历服务支持的协议列表，将服务支持的每一种协议发布到多个注册中心
> org.apache.dubbo.config.ServiceConfig.doExportUrlsFor1Protocol

SCOPE_LOCAL 本地发布injvm
> org.apache.dubbo.config.ServiceConfig.exportLocal

SCOPE_REMOTE 远程发布
根据registryURL获取到Invoker对象，
> org.apache.dubbo.rpc.ProxyFactory.getInvoker
> 包装Invoker
> org.apache.dubbo.config.invoker.DelegateProviderMetaDataInvoker

根据registry Invoker获取到RegistryProtocol
> org.apache.dubbo.rpc.Protocol.export
>
> 注：protocol为自适应扩展点，所以会根据registryURL中的registry协议来获取对应的protocol，具体顺序如下：
> QosProtocolWrapper -> ProtocolFilterWrapper -> ProtocolListenerWrapper -> RegistryProtocol
>

实质发布服务的方法，会打开Netty Server的端口等
> org.apache.dubbo.registry.integration.RegistryProtocol.doLocalExport
>
> org.apache.dubbo.rpc.Protocol.export
>
> 注：protocol为自适应扩展点，会根据providerUrl获取到DubboProtocol对象来发布服务
>
> 
> org.apache.dubbo.rpc.protocol.dubbo.DubboProtocol.openServer

注册服务
> org.apache.dubbo.registry.integration.RegistryProtocol.register
>
> 根据registryUrl获取到具体的注册中心Factory对象，如ZookeeperRegistryFactory、RedisRegistryFactory等
>
> 不同的Registry通过继承FailbackRegistry来进行注册
> org.apache.dubbo.registry.support.FailbackRegistry.register
>
> org.apache.dubbo.registry.support.FailbackRegistry.doRegister


# 服务订阅流程解读 - ReferenceConfig

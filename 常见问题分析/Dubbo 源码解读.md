## Dubbo SPI 机制

- 核心类
  - ExtensionLoader



- 自动包装扩展点

> 自动包装扩展点的 Wrapper 类。`ExtensionLoader` 在加载扩展点时，如果加载到的扩展点有拷贝构造函数，则判定为扩展点 Wrapper 类。
>
> Wrapper 类同样实现了扩展点接口，但是 Wrapper 不是扩展点的真正实现。它的用途主要是用于从 `ExtensionLoader` 返回扩展点时，包装在真正的扩展点实现外。即从 `ExtensionLoader` 中返回的实际上是 Wrapper 类的实例，Wrapper 持有了实际的扩展点实现类。
>
> 通过 Wrapper 类可以把所有扩展点公共逻辑移至 Wrapper 中。新加的 Wrapper 在所有的扩展点上添加了逻辑，有些类似 AOP，即 Wrapper 代理了扩展点。

- 自动装配扩展点

> 加载扩展点时，自动注入依赖的扩展点。加载扩展点时，扩展点实现类的成员如果为其它扩展点类型，`ExtensionLoader` 在会自动注入依赖的扩展点。`ExtensionLoader` 通过扫描扩展点实现类的所有 setter 方法来判定其成员。即 `ExtensionLoader` 会执行扩展点的拼装操作。

- 普通扩展点

> 

- 自适应扩展点 

> Dubbo 使用 URL 对象（包含了Key-Value）传递配置信息。扩展点方法调用会有URL参数（或是参数有URL成员），可根据URL参数动态来获取扩展点的key，然后扩展点方法执行时才决定调用哪一个扩展点实现。
>
> 在 Dubbo 的 `ExtensionLoader` 的扩展点类对应的 `Adaptive` 实现是在加载扩展点里动态生成。指定提取的 URL 的 Key 通过 `@Adaptive` 注解在接口方法上提供。

- 自动激活扩展点

> 对于集合类扩展点，比如：`Filter`, `InvokerListener`, `ExportListener`, `TelnetHandler`, `StatusChecker` 等，可以同时加载多个实现，此时，可以用自动激活来简化配置。
>
> 自动激活扩展点，会激活多个扩展点实现，支持order排序，支持group过滤，这一个扩展点集合组成一条链路，变相的实现责任链模式。
>
> 思考：openapi-gateway 中对于自定义扩展点GatewayRouter就是属于自动激活扩展点，该自定义Route被插入到了Route链中来进行路由。
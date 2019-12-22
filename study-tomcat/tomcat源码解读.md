# 官方文档

> 请求流程图：
>
> https://tomcat.apache.org/tomcat-8.5-doc/architecture/requestProcess/request-process.png

> 
>
> https://tomcat.apache.org/tomcat-8.5-doc/architecture/startup.html



# Tomcat优化

> 注：要优化，首先要清除评判Tomcat好坏的指标是什么？

Tomcat 性能指标

> 吞吐量、响应时间、线程池、CPU、内存等

命令

> ps -ef | grep tomcat
>
> cat /pro/pid/status
>
> top -H -p pid  -> 查看pid下线程资源使用情况
>
> jstack pid -> 查看线程的堆栈信息，线程状态等

工具

> jconsole , jvisualvm , arthas 等



## 基于 `server.xml` 文件进行优化

- Connector

  > IO 优化: BIO/NIO/Apr
  >
  > jmeter压测：观察吞吐量和响应时间，在不同并发情况下，使用不同的IO方式，分析出最佳值。
  >
  > 

## 基于 `web.xml` 文件进行优化

> 静态资源配置
>
> 无用的servlet
>
> session配置

## JVM 优化

> JVM优化同样使用于Tomcat

> 数据优化



# Tomcat核心组件

> - 核心接口 -> server.xml 

```java
Server
	Service
		Connector -> 连接器
		Container -> 容器
            Engine -> 请求转发、负载
                Host -> 机器 或 域名
                    Context -> web项目（配置path，本地的目录）
                        Wrapper -> Servlet -> 包装器
```

```java
Tomcat所有组件都有实现 Lifecycle生命周期接口:

Lifecycle -> 一键式管理 - 启停
	init()
	start()
	stop()
	destory()
    
LifecycleBase -> Lifecycle基础实现类
```



## 自定义类加载器

> 自定义类加载器 -> org.apache.catalina.loader.WebappClassLoader 
>
> 优先加载自己下的类，再去加载父加载器下的类。
>
> WebappClassLoader  -> 隔离应用中类的类加载器
>
> SharedClassLoader ->  共享应用中类的类加载器
>
> CatalinaClassLoader -> 隔离Tomcat中类的类加载器



- Tomcat打破双亲委派模型

> 分析：
>
> 因为一个tomcat中可以部署多个应用，若不同应用中存在相同的类，则不同应用的类需要共存，也就是要做隔离，针对不同应用都创建一个WebappClassLoader类加载器.
>
> 某些场景下需要共享不同应用下的类：
>
> 比如spring的类需要在不同应用中共享，所以需要有一个应用间共享的类加载器SharedClassLoader 



> - JDK双亲委派模型
>
> Bootstrap ClassLoader	-> 根类加载器
>
> Ext ClassLoader				-> 扩展类加载器
>
> App ClassLoader				-> 应用类加载器
>
> - Tomcat自定义类加载器
>
> CommonClassLoader 		-> Tomcat和Web应用的共享
>
> ​		CatalinaClassLoader   	-> Tomcat和Web应用的隔离
>
> ​		SharedClassLoader 		-> web应用之间类的隔离，希望类只有一份，如：spring中的类
>
> ​				WebAppClassLoader 	-> web应用之间类的隔离



# Tomcat源码

> 特别注意：阅读Tomcat源码时请 `server.xml` 文件中定义的 `核心组件之间的包含层次` 来阅读代码的跳转。

- 加载/conf/catalina.properties配置文件

  > org.apache.catalina.startup.CatalinaProperties#loadProperties -> 静态方法 - > 
  >
  > 加载conf/catalina.properties配置文件 -> 
  >
  > 设置配置属性到系统属性变量中  -> 用于后续代码从系统属性中获取变量
  >
  > 通过 System.setProperty(key,value)  设置到系统属性变量中

- Bootstrap#main -> Tomcat入口

  > org.apache.catalina.startup.Bootstrap#main
  >
  > 实质就是一个main方法入口，不用觉得有什么神奇的地方。

- Bootstrap#init() -> Bootstrap初始化

  > org.apache.catalina.startup.Bootstrap#init()
  >
  > 初始化自定义类加载器 -> org.apache.catalina.loader.WebappClassLoader -> 打破双亲委派模型
  >
  > 目的：因为一个tomcat中可以部署多个应用，如果不同应用中存在相同的类
  >
  > 通过反射实例化启动类 - > org.apache.catalina.startup.Catalina 

- Bootstrap#load -> 加载

  > org.apache.catalina.startup.Bootstrap#load()
  >
  > 通过反射获取 `Catalina#load()` 方法并执行
  >
  > 初始化Digester消化器 -> org.apache.catalina.startup.Catalina#createStartDigester -> Digester
  >
  > > 此方法定义了后续将进行的操作
  > >
  > > 将从server.xml中解析出来的各个组件，通过反射的方式设置到各个对象中。
  > >
  > > Catalina 添加 Server -> org.apache.catalina.startup.Catalina#setServer
  > >
  > > Server 添加 Service -> org.apache.catalina.core.StandardServer#addService
  > >
  > > Service 添加 Connector -> org.apache.catalina.core.StandardService#addConnector
  > >
  > > Service 添加 Executor -> org.apache.catalina.core.StandardService#addExecutor
  > >
  > > Service 添加 Engine-> org.apache.catalina.core.StandardService#setContainer
  > >
  > > Engine 添加 Host -> org.apache.catalina.core.StandardEngine#addChild
  > >
  > > Host 添加 Context-> org.apache.catalina.core.StandardHost#addChild
  > >
  > > Context 添加 Wrapper-> org.apache.catalina.core.StandardContext#addChild
  > >
  > > 注：Wrapper 是对 Servlet 的包装。
  >
  > 加载conf/server.xml -> org.apache.catalina.startup.Catalina#configFile
  >
  > 解析conf/server.xml -> org.apache.tomcat.util.digester.Digester#parse()
  >
  > 基于初始化Digester时，定义的组件加载顺序来和初始化并设置各个组件。

- Catalina#getServer().init() -> Server初始化

  > org.apache.catalina.startup.Catalina#getServer() -> 获取 `Server`
  >
  > org.apache.catalina.Lifecycle#init() -> 组件初始化，调用 `initInternal()` -> 实际为 `StandardServer.initInternal()`
  >
  > org.apache.catalina.core.StandardServer#initInternal
  >
  > org.apache.catalina.core.StandardService#initInternal
  >
  > org.apache.catalina.core.StandardThreadExecutor#initInternal
  >
  > org.apache.catalina.core.ContainerBase#initInternal
  >
  > 
  >
  > 按照组件层次结构，一层一层的去调用init()方法区初始化各个组件：
  >
  > Server -> service -> Engine/Executor/Connector  -> Host  -> Context -> Wrapper
  >
  > 

- Bootstrap#start -> 启动

  > org.apache.catalina.startup.Bootstrap#start
  >
  > 通过反射获取 `Catalina#start()` 方法并执行

- 


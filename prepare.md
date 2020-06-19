JUC
> Synchronized、volatile(禁止指令重排序/内存屏障)、JMM(Java内存模型)、AQS、ReentrantLock、Condition、CHM-ConcurrentHashmap、CAS、Atomic

Redis
> 数据类型+存储原理、分布式锁、持久化、hash槽、集群、过期策略、内存淘汰策略、IO模型

Zookeeper
> 源码、分布式锁、Leader选举、watch机制、Zookeeper集群本身的选举

Kafka
> Producer、Consumer Group、Topic、Partition、分区副本、ISR副本集数据同步、ISR副本集Leader选举、rebalance、
>
> 持久化、顺序读写、页缓存、零拷贝、重置offset、发送消息的确认机制、消息消费方式pull、批量pull、消息消费确认方式（ack自动/手动）

序列化
> Java序列化、Protobuf、Hessian、Json、Xml
>
> 序列化关键指标：序列化速度、码流大小(压缩/网络传输)

RPC
> 服务发现、负载均衡、网络通信（BIO/NIO/Netty）、序列化、手写RPC

Dubbo

配置中心
> 配置新增、配置修改、配置审核、配置发布、应用中配置同步(pull/push)、配置回滚、网络通信

注册中心
> 服务注册与发现；心跳检测；

JVM
> 类加载机制、JVM结构、堆内存、栈内存、元空间、程序计数器、Java对象结构(MarkWord)

NIO
> Selector、Channel、ByteBuf、IO多路复用(select/poll/epoll)(空轮询问题)事件驱动、手写RPC实现

Netty
> 手写RPC实现、IM

Spring
> 源码：容器、AOP、BeanPostProcesser、ApplicationListener/ApplicationEvent、设计模式；手写Spring；

Mybatis
> 源码：Plugins、加载机制、设计模式、一级缓存；手写Mybatis；

MYSQL
> Hash/B树/B+树；

SpringCloud/Feign/Robbin/Eureka

SpringBoot
> SpringBoot加载过程；自定义starter；核心特点；

数据结构
> 时间复杂度、空间复杂度；红黑树、B+树、完全二叉平衡树；

设计模式
> 一招鲜吃遍天；工厂模式、策略模式、单例模式、适配器模式、模板方法模式、代理模式、责任链模式、观察者模式（事件监听模式）

项目实战
> 业务中台实战、技术中台实战、开放平台实战、SAAS平台实战、服务容器化实战、容器编排实战
>
>


花生日记电商平台

项目描述

> 花生日记是一个拥有6700万用户的社交电商平台，以优惠、导购、社交为核心。随着业务的发展，构建了电商业务中台，以供业务更高效的进行探索和创新。

> 花生日记是一个拥有6700万用户的社交电商平台，以优惠、导购、社交为核心。为了应对公司在自营电商、在线教育等业务领域的探索，构建了电商业务中台，将核心能力随着业务发展沉淀到平台，供业务更高效的进行探索和创新。

> 花生日记APP、社区团购、花生直邮、课代表、花享卡、星选、智慧云埔

责任描述

配置中心、注册中心、调度中心、RPC组件、定制版Kafka、应用网关、下单结算服务、售后中心

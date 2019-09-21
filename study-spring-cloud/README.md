# spring-cloud

#### spring-cloud学习的参考
https://github.com/forezp/SpringCloudLearning

### 熔断、限流的思考

- 单机熔断限流：
    - 基于AOP+自定义注解实现单机版的熔断和限流策略
    - 1、超时熔断 -> 基于juc中的Future.get(timeout,TimeUnit来实现超时处理机制)
    - 2、方法限流 -> 基于Semaphore信号量实现限流，也就是所谓的TPS/QPS
    - 3、IP限流

- 分布式熔断限流：
    - 基于单机熔断限流进行扩展
    - 需要有一个 配置中心 来存储着分布式环境下同一个资源的熔断和限流配置，如sentinel
    - 同一个资源，可以简单理解为是同一个服务下同一个接口的同一个方法
    
- 问题：怎么实现熔断限流的动态调整？
  
  分析：可以通过nacos的subscribe或zookeeper的watch来实现分布式熔断限流配置的变更通知
        单机的话，可以通过配置文件的形式来实现


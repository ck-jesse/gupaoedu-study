# rpc 常见问题解答

### 1、协议
    七层网络协议
    http协议
    rpc协议：
    tcp/ip
    

#### 2、http和https
    答：

#### 3、数据序列化
    java原生序列化
    hessian
    fastjson
    jackson
    gson
    xml
    protobuf
    不同的序列化后的数据大小不同，越小传输性能越高
    实现样例，体现对象转字节流后的大小，以及数据格式也会影响大小
    
#### 4、基于zk实现服务注册与发现
    1、减少服务端host/port的配置
    2、增加灵活性和可扩展性
    3、实现针对多个应用提供的服务实现负载均衡算法（随机）
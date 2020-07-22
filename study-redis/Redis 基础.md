
https://redisbook.readthedocs.io/en/latest/index.html

# 存储简介

- KV 存储 
  - redis
  - memcached
- 文档存储
- 列存储
  - HBase
- 图存储
  - graph 

# Redis文档
> Redis 命令参考 http://redisdoc.com/
>
> Redis 中文网站 http://www.redis.cn/


# Redis 基础

支持持久化

Redis并不是简单的key-value存储，实际上他是一个数据结构服务器，支持不同类型的值。也就是说

## 数据结构类型

### 字符串 - Strings

> - 二进制安全的字符串
>
> 最简单Redis类型。如果你只用这种类型，Redis就像一个可以持久化的memcached服务器。

#### 存储原理

- value最大长度为 512M
- 查看key的编码
  - object encoding keyname
- embstr 编码
  - 一个字符
  - 只读
- raw 编码
  - 44个字节

#### 应用场景

- 缓存
- 分布式session
- setNX
  - 只有key存在时，才将key的值设置为value
  - 设置成功返回1，设置失败返回0
- setEX - 过期时间单位秒
  - SETEX 相当于 set key value + expire key seconds 两个命令的效果
  - SETEX 是一个原子操作，它可以在同一时间内完成设置值和设置过期时间这两个操作， 因此 `SETEX` 命令在储存缓存的时候非常实用。
- incr 全局ID
- incr 计数器
- incr 限流
- 位操作 - setbit - 节省空间



### 哈希表 - Hashes

由field和关联的value组成的map。field和value都是字符串的。

#### 存储原理

- ziplist - 压缩列表
  - 压缩 - 节省空间
- Hash
  - 数组 + 链表 的数据结构
  - 扩容
  - dict
    - dictht - dict hashtable
      - ht[0]
      - ht[1]
    - dictEntry

#### 应用场景

- 存放用户信息 - session



### 列表 - Lists

按插入顺序排序的字符串元素的集合。他们基本上就是*链表（linked lists）*。

#### 存储原理

- quicklist - 双向链表
  - head
  - tail
  - count
  - len
  - quicklistNode
    - pre
    - tail

#### 应用场景

- LIFO - 栈
  - 后进先出 - 栈
  - lpush - 左边存 - 插入到表头
  - lpop - 左边取 - 移除并返回头元素
- FIFO - 队列
  - 先进先出 - 可用作消息队列
  - rpush - 右边存 - 插入到表尾
  - lpop - 左边取 - 移除并返回头元素

### 集合 - Sets

不重复且无序的字符串元素的集合。

#### 存储原理

#### 应用场景

- 用户关注
  - 相互关注
  - 
- 推荐模型

### 有序集合 - Sorted sets

类似Sets,但是每个字符串元素都关联到一个叫*score*浮动数值（floating number value）。里面的元素总是通过score进行着排序，所以不同的是，它是可以检索的一系列元素。（例如你可能会问：给我前面10个或者后面10个元素）。

#### 存储原理

- ziplist - 压缩列表

- skiplist + dict - 跳表 + 字典

  - skiplist - 跳表
  - 跳表是在有序链表的结构下，给某些元素增加level层级，然后在查找的时候先从高level层级往下查找，3->2->1，该方法是以空间换时间。
  - 跳表总元素的level层级是怎么确定的呢？
    - 是通过随机生成的level的

  > 注意：二分查找 只适用于有序列表，不适用与有序链表

- 

#### 应用场景

- 有序集合 - score

## 热点数据
- redis 的 monitor 命令
- facebook 开源工具 redis-faina
## 缓存雪崩
- random 增加随机时间
- 永不过期
- 预更新

## 缓存击穿-缓存穿透
> 说明：缓存数据库中不存在的数据时，这种情况就叫做缓存穿透，也就是每次都会去数据库去查询
- 设置一个null值得缓存，并设置一个较短的过期时间
- 通过布隆过滤器来过滤掉非法的key
> 布隆过滤器的本质是通过 位图BitMap 来实现的，将key通过多次hash计算后保存到BitMap中，
> 后面的请求进来时，就通过对key进行hash计算，然后判断key是否存在BitMap中
> 注意：布隆过滤器需要提前初始化数据到其中；
> 布隆过滤器存在误判率的问题，也就是原本不存在的元素，但是判断为存在，实质是hash碰撞导致的。
> 如果布隆过滤器判断不存在，则一定不存在，如果判断存在，则可能不存在（误判）



## Redis实现延迟消息队列

> Redis可以通知发布/订阅客户端关于键空间中发生的事件，如果Redis开启了键空间事件通知，且客户端订阅了某些键的事件，则在相应的键发生变动时，会通过发布/订阅向客户端发送两条消息：
>
> PUBLISH __keyspace@0__:foo del  -- 键空间通知
>
> PUBLISH __keyevent@0__:del foo  -- 键事件通知
>
> 当开启键空间通知功能时，需要额外的消耗一些CPU，所以此功能默认为关闭状态，可以通过修改redis.conf文件或者使用config set命令来开启或关闭键空间通知功能
>
> 当notify-keyspace-events的值为空字符串时，功能关闭
>
> 当参数的值不是空字符串时，功能开启，且参数的值的取值范围是固定的

 参数的可选值

 输入的参数中至少要有一个K或E来指定通知类型，否则配置不会生效

| 字符 | 通知事件                                                   |
| ---- | ---------------------------------------------------------- |
| K    | 键空间通知，所有通知以 __keyspace@__ 为前缀                |
| E    | 键事件通知，所有通知以 __keyevent@__ 为前缀                |
| g    | DEL 、 EXPIRE 、 RENAME 等类型无关的通用命令的通知         |
| $    | 字符串命令的通知                                           |
| l    | 列表命令的通知                                             |
| s    | 集合命令的通知                                             |
| h    | 哈希命令的通知                                             |
| z    | 有序集合命令的通知                                         |
| x    | 过期事件：每当有过期键被删除时发送                         |
| e    | 驱逐(evict)事件：每当有键因为 maxmemory 政策而被删除时发送 |
| A    | 参数 g$lshzxe 的别名                                       |
|      |                                                            |

在Redis中有两种方式将key删除：

1. 当一个键被访问时，Redis会对这个键进行检查，如果键已经过期，则将该键删除
2. Redis后台会定期删除那些已经过期的键

当过期键被删除时，Redis会产生一个expired通知。在此要理解一点，就是并不是当key的TTL变为0时就会立即被删除，所以Redis产生expired通知的时间为键被删除的时候而不是键的TTL变为0的时候。

依据上述表格，我们可以将`notify-keyspace-events`设置为`Ex`，表示键过期事件通知。



##### Java应用中通知监控

Spring Data Redis 实现发布订阅功能非常简单 

-  创建`RedisMessageListenerContainer`实例 

```java
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisListenerConfig {

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }

}
```



- 创建key过期事件监听器

```java
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();
        System.out.println("监听到key: " + key + " 过期!");
    }
}
```


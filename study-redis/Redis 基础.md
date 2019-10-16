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

> 文档地址
>
> https://www.yiibai.com/guava/guava_optional_class.html
>
> Guava官方教程
> https://ifeve.com/google-guava/
>
> http://ifeve.com/google-guava-cachesexplained/

## Guava Cache的不足之处
> 1、是单个应用运行时的本地缓存，数据没有持久化存放到文件或外部服务器
> 
> 2、单机缓存，受机器内存限制，重启应用缓存数据会丢失，应用分布式部署会出现缓存数据不一致的情况



## LoadingCache#get 方法原理分析
> com.google.common.cache.LoadingCache#get
>
> com.google.common.cache.LocalCache.Segment#get(K, int, com.google.common.cache.CacheLoader<? super K,V>)
>
> com.google.common.cache.LocalCache.Segment#getEntry
>
> com.google.common.cache.LocalCache.Segment#tryDrainReferenceQueues
>
> referenceQueue.poll()清理引用队列中的键和值
>
> com.google.common.cache.LocalCache.Segment#drainReferenceQueues
>
> com.google.common.cache.LocalCache.Segment#drainKeyReferenceQueue
>
> com.google.common.cache.LocalCache#reclaimKey
>
> com.google.common.cache.LocalCache.Segment#reclaimKey
>
> 清除writeQueue和accessQueue包含被垃圾收集的键或值的内部条目
>
> com.google.common.cache.LocalCache.Segment#removeValueFromChain
>
注：基于ReferenceQueue引用队列，实现在缓存对象被垃圾回收器回收后，将对应的Reference引用对象添加到引用队列中，然后将清除缓存队列中被回收的key和value


## RateLimiter 分析

com.google.common.util.concurrent.RateLimiter
```
acquire() // 从RateLimiter获取一个许可，该方法会被阻塞直到获取到请求。返回获取许可的睡眠时间
tryAcquire() // 从RateLimiter 获取许可，如果该许可可以在无延迟下的情况下立即获取得到的话。
tryAcquire(int permits) // 从RateLimiter 获取许可数，如果该许可数可以在无延迟下的情况下立即获取得到的话。
tryAcquire(long timeout,TimeUnit unit) // 从RateLimiter获取许可如果该许可可以在不超过timeout的时间内获取得到的话，或者如果无法在timeout 过期之前获取得到许可的话，那么立即返回false（无需等待）。
```

com.google.common.base.Ticker
```
read() // 读取系统当前时间(纳秒)
```

com.google.common.base.Stopwatch
```
startTick // 开始时间
elapsedNanos // 实耗时间(默认0)(通过源码发现只有在执行stop()后才会赋值)
elapsed(java.util.concurrent.TimeUnit.MICROSECONDS) // 获取实耗时间(微秒)（当前时间 - 开始时间 + 实耗时间）
toString() // 返回当前运行时间的字符串表示形式
```

com.google.common.util.concurrent.SmoothRateLimiter
```
storedPermits // 当前存储的许可证
nextFreeTicketMicros // 下一个免费令牌微秒
resync() // 基于当前时间更新 storedPermits 和 nextFreeTicketMicros
```


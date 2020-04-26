Guava Cache
> Caffeine是一个基于Google开源的Guava设计理念的一个高性能内存缓存，使用java8开发，spring boot引入Caffeine后已经逐步废弃Guava的整合了。

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

> SmoothBursty 的核心设计思想基本与令牌桶类似，但还是有些不同。
>
> 基本思想：
> SmoothBursty 以指定的速率生成许可，在 SmoothBursty 中用 storedPermits 表示。
> 
> 当一个请求需要申请许可时，如果需要申请的许可数小于 storedPermits ，则消耗指定许可，直接返回，无需等待。
> 
> 当一个请求需要申请的许可大于 storedPermits 时，则计算需要等待的时间，更新下一次许可可发放时间，直接返回，即当请求消耗掉所有许可后，当前请求并不会阻塞，而是影响下一个请求，即支持突发流量。
  


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
nextFreeTicketMicros // 下一次可以免费获取许可的时间，所谓的免费指的是无需等待就可以获取设定速率的许可，该方法对理解限流许可的产生非常关键
stableIntervalMicros // 发放一个许可所需要的时间，根据 TPS 算出一个稳定的获取1个许可的时间，以一秒发放5个许可，即限速为5TPS，那发放一个许可的世界间隔为 200ms，stableIntervalMicros 变量是以微秒为单位。
resync() // 基于当前时间更新 storedPermits 和 nextFreeTicketMicros
```

```java
final long reserveEarliestAvailable(int requiredPermits, long nowMicros) {
    resync(nowMicros);
    long returnValue = nextFreeTicketMicros;
    // 计算本次能从 storedPermits 中消耗的许可数量，取需要申请的许可数量与当前可用的许可数量的最小值
    double storedPermitsToSpend = min(requiredPermits, this.storedPermits);
    // 如果申请的许可数量( requiredPermits )大于当前可用的许可数量( storedPermits )，则还需要等待新的许可生成
    double freshPermits = requiredPermits - storedPermitsToSpend;
    // 计算本次申请需要等待的时间 = 不足的许可数量 * 发放一个许可所需要的时间
    long waitMicros =
        storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend)
            + (long) (freshPermits * stableIntervalMicros);

    // 更新 nextFreeTicketMicros 为当前运行时间加上需要等待的时间
    this.nextFreeTicketMicros = LongMath.saturatedAdd(nextFreeTicketMicros, waitMicros);
    // 减少本次已消耗的许可数量
    this.storedPermits -= storedPermitsToSpend;
    // 请注意这里返回的 returnValue 的值，并没有包含由于剩余许可需要等待创建新许可的时间，即允许一定的突发流量，
    // 故本次计算需要的等待时间将对下一次请求生效，这也是框架作者将该限速器取名为 SmoothBursty 的缘由。
    return returnValue;
}
// 先根据当前时间即发放许可速率更新 storedPermits 与 nextFreeTicketMicros（下一次可以免费获取许可的时间）。
void resync(long nowMicros) {
    // if nextFreeTicket is in the past, resync to now
    // 如果当前已运行时间大于 nextFreeTicketMicros（下一次可以免费获取许可的时间），则需要重新计算许可，即又可以向许可池中添加许可。
    if (nowMicros > nextFreeTicketMicros) {
      // 根据当前时间计算可增加的许可数量，也就是以指定的速率生成许可
      // coolDownIntervalMicros() 方法返回的就是上文提到的 stableIntervalMicros (发放一个许可所需要的时间)
      // 本次可以增加的许可数 = 当前已运行时间 - nextFreeTicketMicros 
      double newPermits = (nowMicros - nextFreeTicketMicros) / coolDownIntervalMicros();
      // 计算当前可用的许可数
      storedPermits = min(maxPermits, storedPermits + newPermits);
      // 更新下一次可获取计算许可的时间
      nextFreeTicketMicros = nowMicros;
    }
}
```

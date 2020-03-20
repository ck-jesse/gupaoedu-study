
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

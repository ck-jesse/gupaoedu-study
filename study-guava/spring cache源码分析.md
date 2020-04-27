# Spring Cache源码分析

```text

入口
org.springframework.cache.annotation.CachingConfigurationSelector.selectImports

缓存配置
org.springframework.cache.annotation.ProxyCachingConfiguration

缓存注解操作来源（定义注解解析器）
org.springframework.cache.annotation.AnnotationCacheOperationSource.AnnotationCacheOperationSource(boolean)

注解解析器
org.springframework.cache.annotation.SpringCacheAnnotationParser.parseCacheAnnotations(org.springframework.cache.annotation.SpringCacheAnnotationParser.DefaultCacheConfig, java.lang.reflect.AnnotatedElement, boolean)


```


```java
// org.springframework.cache.interceptor.CacheAspectSupport.execute(org.springframework.cache.interceptor.CacheOperationInvoker, java.lang.reflect.Method, org.springframework.cache.interceptor.CacheAspectSupport.CacheOperationContexts)
public abstract class CacheAspectSupport extends AbstractCacheInvoker
		implements BeanFactoryAware, InitializingBean, SmartInitializingSingleton {

    // 省略...

    // 执行
	private Object execute(final CacheOperationInvoker invoker, Method method, CacheOperationContexts contexts) {
		// Special handling of synchronized invocation
        // 同步调用
		if (contexts.isSynchronized()) {
			CacheOperationContext context = contexts.get(CacheableOperation.class).iterator().next();
			if (isConditionPassing(context, CacheOperationExpressionEvaluator.NO_RESULT)) {
				Object key = generateKey(context, CacheOperationExpressionEvaluator.NO_RESULT);
				Cache cache = context.getCaches().iterator().next();
				try {
                    // 获取或加载数据
                    // 当数据不存在时，通过Callable加载数据(锁，即只有一个线程会执行)，然后设置到缓存，再返回数据
                    // org.springframework.cache.Cache.get(key, Callable<T>) 由具体的实现类去控制并发调用(GuavaCache/CaffeineCache)
                    // 举例：
                    // Guava Cache 获取数据时支持如下两种方式：
                    // com.google.common.cache.CacheLoader.load(key) // 在创建LoadingCache对象时指定数据加载方式，相对固定 
                    // com.google.common.cache.Cache.get(key, Callable) // 在获取数据时指定数据加载方式，此方式更加灵活
                    // GuavaCache采用该方式，实质为将Callable通过CacheLoader进行包装后再执行的数据加载
					return wrapCacheValue(method, cache.get(key, () -> unwrapReturnValue(invokeOperation(invoker))));
				}
				catch (Cache.ValueRetrievalException ex) {
					// The invoker wraps any Throwable in a ThrowableWrapper instance so we
					// can just make sure that one bubbles up the stack.
					throw (CacheOperationInvoker.ThrowableWrapper) ex.getCause();
				}
			}
			else {
				// No caching required, only call the underlying method
				return invokeOperation(invoker);
			}
		}

        // 并发调用（并发时相同的key会加载多次，后执行的线程会覆盖前面的数据）
		// Process any early evictions
		processCacheEvicts(contexts.get(CacheEvictOperation.class), true,
				CacheOperationExpressionEvaluator.NO_RESULT);

		// Check if we have a cached item matching the conditions
		Cache.ValueWrapper cacheHit = findCachedItem(contexts.get(CacheableOperation.class));

		// Collect puts from any @Cacheable miss, if no cached item is found
		List<CachePutRequest> cachePutRequests = new LinkedList<>();
		if (cacheHit == null) {
			collectPutRequests(contexts.get(CacheableOperation.class),
					CacheOperationExpressionEvaluator.NO_RESULT, cachePutRequests);
		}

		Object cacheValue;
		Object returnValue;

		if (cacheHit != null && !hasCachePut(contexts)) {
			// If there are no put requests, just use the cache hit
			cacheValue = cacheHit.get();
			returnValue = wrapCacheValue(method, cacheValue);
		}
		else {
			// Invoke the method if we don't have a cache hit
			returnValue = invokeOperation(invoker);
			cacheValue = unwrapReturnValue(returnValue);
		}

		// Collect any explicit @CachePuts
		collectPutRequests(contexts.get(CachePutOperation.class), cacheValue, cachePutRequests);

		// Process any collected put requests, either from @CachePut or a @Cacheable miss
		for (CachePutRequest cachePutRequest : cachePutRequests) {
			cachePutRequest.apply(cacheValue);
		}

		// Process any late evictions
		processCacheEvicts(contexts.get(CacheEvictOperation.class), false, cacheValue);

		return returnValue;
	}
    // 省略...
}
```
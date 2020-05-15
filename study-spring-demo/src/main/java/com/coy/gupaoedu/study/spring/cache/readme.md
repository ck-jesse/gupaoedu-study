思考：
A节点获取缓存，本地缓存和redis中无缓存，则加载缓存，并添加到本地缓存和redis
B节点获取缓存，本地缓存无，redis中有，则添加到本地缓存

是否可以通过LoadingCache.refresh(key) 来刷新缓存，结合redis的发布订阅来保证缓存的一致性
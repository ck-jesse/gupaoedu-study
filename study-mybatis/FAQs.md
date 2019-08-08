# mybatis

### 核心类
    SqlSessionFactoryBuilder
    Configuration
    SqlSessionFactory
    SqlSession
    MapperRegistry
    MapperProxyFactory
    MapperProxy
    Interceptor
    Plugin
    InterceptorChain.pluginAll 对目标对象进行拦截，实质为生成代理对象
    Executor/BaseExecutor/SampleExecutor/ReuseExecutor/BatchExecutor
    CacheExecutor 对具体的Executor的包装，一定会执行
    StatementHandler/BaseStatementHandler/CallableStatementHandler/PreparedStatementHandler/SimpleStatementHandler
    RoutingStatementHandler 实例化时根据StatementType来生成具体的StatementHandler，等于含有路由和代理的功能
    Cache
    PerpetualCache 永久缓存，缓存在HashMap中
    
    二级缓存时Cache对象之间的装饰引用顺序，源码见：org.apache.ibatis.mapping.CacheBuilder.build
    SynchronizedCache-->LoggingCache-->SerializedCache-->ScheduledCache-->LruCache-->PerpetualCache
    

### 为什么只需定义Mapper 接口无需实现类就可以访问数据库？
    实质为在初始化时根据Mapper.xml中的namespace（等于Mapper接口的全路径），生成了一个MapperProxyFactory对象，并存放到了MapperRegistry容器中，此时还未创建Mapper接口的代理对象。
    在调用SqlSession.getMapper(Mapper.class)时，实质为通过MapperProxyFactory创建了Mapper接口的代理对象MapperProxy（JDK动态代理），所以无需实现类；
    另外在代理对象MapperProxy中，只需要根据Mapper接口的目标方法的全路径（statementId）就可以找到对应的MapperStatement进行数据库操作，所以只需要定义Mapper接口即可。
    注：在构建SqlSessionFactory的时候，对mybatis配置文件进行了加载解析，并生成了一个Configuration对象来直接或间接维护所有配置（mapper配置文件）。

### 插件机制
    每一个plugin都是一个拦截器，多个plugin组成了一条拦截器链。
    可被拦截的目标对象：Executor/StatementHandler/ParamenterHandler/ResultHandler。
    将符合的 Intecetor 通过jdk动态代理机制对 可被拦截的目标对象 创建代理对象，此时为
    来实现了对代理对象的代理。此处也就解答了的代理对象是否可以被代理的问题（可以）。

### 一级缓存（默认开启）
    一级缓存，又叫本地缓存，是PerpetualCache类型的永久缓存，保存在执行器中（BaseExecutor.localCache），
    而执行器又在SqlSession（DefaultSqlSession）中，所以一级缓存的生命周期与SqlSession是相同的。

### 二级缓存
    二级缓存，又叫自定义缓存，实现了Cache接口的类都可以作为二级缓存，所以可配置如encache等的第三方缓存。
    二级缓存以namespace名称空间为其唯一标识，被保存在Configuration的 Map<String, Cache> caches 核心配置对象中。
    每次构建SqlSessionFactory对象时都会创建新的Configuration对象，因此，二级缓存的生命周期与SqlSessionFactory是相同的。
    在创建每个MapperedStatement对象时，都会根据其所属的namespace名称空间，给其分配Cache缓存对象。
    

### 缓存源码分析：
     注意：CachingExecutor 会代理其他几个Executor，也就是一定会先执行CachingExecutor.
     
     // 目标方法：org.apache.ibatis.executor.CachingExecutor.query
     @Override
     public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, CacheKey key, BoundSql boundSql)
         throws SQLException {
       // 从MappedStatement中获取Cache，该Cache是在Mapper.xml中定义的自定义缓存，也就是我们常说的二级缓存。
       // Cache对象是在加载配置文件的时候初始化并缓存到org.apache.ibatis.session.Configuration.caches容器里面的。
       Cache cache = ms.getCache();
       // cache 不为空，则表示需要使用到自定义的二级缓存
       if (cache != null) {
         flushCacheIfRequired(ms);
         if (ms.isUseCache() && resultHandler == null) {
           ensureNoOutParams(ms, boundSql);
           @SuppressWarnings("unchecked")
           // 从事务缓存管理器中获取缓存的数据
           List<E> list = (List<E>) tcm.getObject(cache, key);
           if (list == null) {
             // 使用BaseExcutor.localCache中的一级缓存，一级缓存是默认开启的
             list = delegate.<E> query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
             tcm.putObject(cache, key, list); // issue #578 and #116
           }
           return list;
         }
       }
       // cache 为空，则表示使用BaseExcutor.localCache 中的一级缓存
       return delegate.<E> query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
     }
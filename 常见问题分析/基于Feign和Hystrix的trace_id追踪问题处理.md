# 概述

在微服务的体系架构中，都存在一个服务与服务之间的调用链路追踪问题。虽然在生产环境中会采用第三方的组件或服务来实现链路追踪，比如`SkyWalking`、`阿里云arms`等，但一旦脱离开这些第三方的功能，微服务体系中的问题排查将会变得异常艰辛，所以如果能在应用维度实现自己的链路追踪，再结合第三方的链路追踪功能，定位问题将变得更加简单和快速。同时在开发、测试、联调过程中也能减少很多沟通成本。



# 方案

**核心思想：将`trace_id`设置到请求头中透传给下游服务，下游服务按同样的方式再透传给下游服务。**

步骤：

1、当请求达到网关时，网关中的拦截器会从请求头中获取trace_id，如果为空，则生成一个trace_id，并设置到SLF4J的MDC中，在网关转发请求时将trace_id设置到请求头中。

2、当请求达到服务A时，应用中的拦截器会从请求头中获取trace_id，如果为空，则生成一个trace_id，并设置到SLF4J的MDC中，如果服务A调用服务B/服务C，那么需要将trace_id设置到请求头中。

3、其他服务（服务B/服务C）的处理方式与步骤2的处理方式一致。

将trace_id设置到MDC中的目的是为了SLF4J在打印日志时将trace_id打印出来。

> 注：MDC的本质是ThreadLocal。



# 具体实现

因为不同的通信方式会导致trace_id的传递实现不一样，结合实际请求方式具体如下：

目前公司的微服务架构是基于Spring Cloud来实现的，所以需要对Feign和Hystrix进行扩展

## 1、Filter实现trace_id拦截

拦截器

```java
public class ServletTraceInfoAttachmentFilter implements Filter {

    public ServletTraceInfoAttachmentFilter() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest requestHttp = (HttpServletRequest) request;
        // 默认从请求中获取trace_id（nginx中生成trace_id）
        String traceId = requestHttp.getHeader(TracingVariable.TRACE_ID);
        try {
            // 当请求中无trace_id时，默认生成一个
            if (traceId == null) {
                traceId = genTraceId();
            }
            MDCLogTracerUtil.attachTraceId(traceId);
            chain.doFilter(request, response);
        } finally {
            MDCLogTracerUtil.removeTraceId();
        }
    }

    /**
     * 生成trace_id
     */
    private String genTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
```

配置

```java

@Configuration
public class TraceIdConfig {
    /**
     * 配置http请求日志跟踪信息拦截器，header中无trace_id则生成
     */
    @Bean
    public FilterRegistrationBean logFilterRegister() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ServletTraceInfoAttachmentFilter());
        registration.setName(ServletTraceInfoAttachmentFilter.class.getSimpleName());
        registration.addUrlPatterns("/*");
        // 从小到大的顺序来依次过滤
        registration.setOrder(1);
        return registration;
    }
}
```



## 2、RestTemplate的链路追踪



```java
/**
  * 基于RestTemplate的服务调用
  */
@Bean("restTemplate")
public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(httpRequestFactory());
    // 将trace_id设置到header中传递到服务提供方
    restTemplate.setInterceptors(Arrays.asList(new ClientHttpRequestInterceptor(){
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            String traceId = MDCLogTracerContextUtil.getTraceId();
            if (traceId != null) {
                request.getHeaders().add(TracingVariable.TRACE_ID, traceId);
            }
            return execution.execute(request, body);
        }
    }));
    return restTemplate;
}
```



## 3、Feign的链路追踪



```java
/**
  * 基于Feign的服务调用
  */
@Bean
public RequestInterceptor requestInterceptor() {
    RequestInterceptor requestInterceptor = template -> {
        String traceId = MDCLogTracerContextUtil.getTraceId();
        if (traceId != null) {
            template.header(TracingVariable.TRACE_ID, traceId);
        }
    };
    return requestInterceptor;
}
```



## 4、Hystrix的链路追踪

本质上是将MDC的内容透传给线程池中的线程。

注：一开始对Feign请求处理，将trace_id设置到header中，但是实际发现在服务提供方的Filter中未获取到trace_id，而是重新生成了一个trace_id，经过排查和debug发现是因为集成了hystrix导致的。

具体原因是由于hystrix是通过线程池来发送请求的，所以trace_id未透传到服务提供方。结合源码分析和百度最终得出如下解决方案。

```java
/**
 * hystrix的线程池传递 MDC的context（如trace_id等）
 *
 */
public class MdcHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                MDC.setContextMap(contextMap);
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }
}
```



```java
/**
  * 注册hystrix的mdc strategy，传递trace_id
  */
@PostConstruct
public void hystrixInit() {
    HystrixPlugins.getInstance().registerConcurrencyStrategy(new MdcHystrixConcurrencyStrategy());
}
```

## 5、Dubbo的链路追踪

服务消费方

```java
/**
 * 服务消费方：附加trace_id的过滤器
 *
 */
@Activate(group = {"consumer"})
public class DubboTraceInfoAttachmentFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DubboTraceInfoAttachmentFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = MDCLogTracerContextUtil.getTraceId();
        LOGGER.debug("Attachment traceId = {}", traceId);

        if (traceId != null) {
            RpcContext.getContext().setAttachment(TracingVariable.TRACE_ID, traceId);
        }
        return invoker.invoke(invocation);
    }
}
```

服务提供方

```java
/**
 * 服务提供方：分离trace_id的过滤器
 *
 */
@Activate(group = {"provider"})
public class DubboTraceInfoDetachmentFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DubboTraceInfoDetachmentFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = RpcContext.getContext().getAttachment(TracingVariable.TRACE_ID);
        LOGGER.debug("Detachment traceId = {}", traceId);

        if (traceId != null) {
            MDCLogTracerContextUtil.attachTraceId(traceId);
        }
        try {
            return invoker.invoke(invocation);
        } finally {
            MDCLogTracerContextUtil.removeTraceId();
        }
    }
}
```

## 6、Spring异步线程池的链路追踪

```java
/**
 * 装饰MDC 内容传递
 */
public class MDCTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        // 对Runnable进行装饰，将主线程的MDC内容设置到子线程的MDC中
        return new Runnable() {
            @Override
            public void run() {
                try {
                    MDC.setContextMap(contextMap);
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            }
        };
    }
}
```



```java
/**
 * 通过实现 AsyncConfigurer 自定义异步任务线程池，包含异常处理
 */
@Component
@Slf4j
public class CustomAsyncConfigurer implements AsyncConfigurer {

    /**
     * 核心线程数
     */
    @Value("${thread.pool.corePoolSize:10}")
    private Integer corePoolSize;

    /**
     * 最大线程数
     * 注：因该线程池主要用于异步将订单结算信息入库，此操作属IO密集型，所以最大线程数可以适当大一些；
     * 假设每个任务执行50ms，则一个线程1s可执行20个任务，100个线程1s可执行2000个任务
     */
    @Value("${thread.pool.maxPoolSize:100}")
    private Integer maxPoolSize;

    /**
     * 队列容量
     */
    @Value("${thread.pool.queueCapacity:15000}")
    private Integer queueCapacity;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setThreadFactory(new NamedThreadFactory("checkout"));
        taskExecutor.setTaskDecorator(new MDCTaskDecorator());//task装饰【重点】
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        // 针对标注 @Async 的方法内出现的异常会进入该异常处理器
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                log.error("线程池执行任务发生异常.", ex);
                for (Object param : params) {
                    log.info("Parameter value - " + param);
                }
            }
        };
    }
}
```


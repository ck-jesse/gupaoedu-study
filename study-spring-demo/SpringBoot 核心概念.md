SpringBoot Bean装载

1、Enable模式

使用：手动定义@EnableCaffeineRedisCache

```java
@EnableCaffeineRedisCache
@Import(CaffeineRedisCacheAutoConfiguration.class) 指定需要装载的类

```



2、自动装配 starter

```java
@SpringBootApplication
@EnableAutoConfiguration
@Import(AutoConfigurationImportSelector.class)
AutoConfigurationImportSelector.selectImports() 返回需要加载的类的全路径
SpringFactoriesLoader.loadFactoryNames(EnableAutoConfiguration.class) 从META-INF/spring.fatories中加载对应的配置类进行自动加载，这是与Enable模式的区别
    
```

> ImportBeanDefinitionRegistar.registerBeanDefinitions()  将packageNames下的路径下的类注册到BeanDefinitionRegistry中

3、条件装配

> @ConditionalOnClass
>
> @ConditionalOnBean
>
> @ConditionalOnProperties

> 通过配置文件META-INF/spring-autoconfigure-metadata.properties来协助条件装配，内容如下：
>
> com.coy.l2cache.spring.L2CacheConfiguration.ConditionalOnClass=com.github.benmanes.caffeine.cache.Caffeine



SPI机制

Java的SPI机制

```java
ServiceProvider
```



spring的SPI机制

SpringFactoriesLoader.loadFactoryNames(class)

dubbo的SPI机制

spring和dubbo都是基于Java的SPI机制的思想来扩展实现的。


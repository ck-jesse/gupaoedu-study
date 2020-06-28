# Spring 常见问题解答

### 1、加载beanDefinition时，对于接口也会创建BeanDefinition吗？
    答：有过滤掉接口和抽象类
    org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider.findCandidateComponents
    org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider.isCandidateComponent(org.springframework.beans.factory.annotation.AnnotatedBeanDefinition)
    判断是否为具体的class(也就是对于接口和抽象类返回false)
    org.springframework.core.type.StandardClassMetadata.isConcrete

#### 2、实例化时，是会将beanDefinitionMap中的所有的定义都创建对象吗？
    答：对于非抽象类、非延迟加载、且为单例模式的类进行创建

#### 3、是否对没有定义spring注解的类创建BeanDefinition？
    答：通过<component-scan>扫描时，只会对定义了@component注解（含标注了@component注解的注解）的类构建BeanDifinition

#### 4、依赖注入时，字段类型定义为接口的字段，是怎么注入的？
    答：1)根据字段类型获取所有的beanNames

#### 5、aop创建代理对象以后，代理中的属性为null，怎么将目标对象的属性赋值给代理对象呢？
    答：代理对象中含有目标对象的引用，虽然创建代理对象后看到的属性为null，但是在实际调用代理对象的方法时，为null的属性可以从目标对象中拿到对应的属性值，这个不知道是怎么获取到并赋值的？

#### 6、两个方法A和B被同一个拦截器拦截到，当A中调用B时，会B的拦截器执行完毕后，其中的Invocation已经赋值为了B的Invocation，所以在A重新拿到执行权限时，其获取到了InvocationB，所以出现了混乱
    答：可以在拦截器链的第一个位置添加一个Invocation拦截器(结合ThreadLocal)，具体的逻辑如下：
     1）从ThreadLocal中将旧的Invocation取出来，并记录下来
     2）设置当前Invocation到ThreadLocal中，保证当前的拦截器能获取到自己的Invocation
     3）当所有的拦截器执行完毕，将旧的Invocation设置到ThreadLocal中，保证外层的拦截器能获取到自己的Invocation

#### 7、A依赖B，B依赖A的循环引用的问题，该怎么解决？？
    答：
       1）在创建单例beanA子后，并在单例beanA的属性beanB注入之前，提前暴露该单例beanA的引用到容器中（提前暴露的单例bean容器），然后在给beanA注入beanB时发现beanB未创建，所以会去创建beanB，
       2）在创建单例beanB后，也提前暴露beanB的引用，然后在给beanB注入beanA时，从提前暴露的单例bean容器中获取beanA的引用，以完成beanA的注入
       注意：若提前暴露的单例bean符合某些aop的拦截规则，则也需要提前创建该单例bean的代理bean且需提前暴露（针对这种情况，在该单例bean初始化initializeBean后，则将该单例bean替换为提前暴露的代理bean，以保持bean一致）
       特别注意：
       1、循环依赖只支持单例bean，且循环依赖只支持属性注入的方式，对于构建函数的注入产生的循环依赖问题不支持
       2、循环依赖不支持prototype类型的bean，因为prototype类型的bean在每次使用时都会创建新的bean，所以无法支持

#### 8、FactoryBean的原理和自定义FactoryBean
    答：在getBean(name)时，判断该bean是否为FactoryBean创建的bean，若是，则获取通过自定义FactoryBean创建该bean，并存放到Map<String, Object> factoryBeanObjectCache中缓存起来，
    后续获取该bean时，直接从该缓存中获取。
    
#### 9、自定义FactoryBean创建的bean，怎么注入到别的bean中？
    答：在获取bean的过程中，会对其中定义了@Autowired或者@Resource注解的属性进行注入，getBean()中会根据字段类型进行自动注入，
    1、调用autowireByType()方法
    2、根据字段类型，从beanDefinitionMap中找到与之类型匹配的beanName，其中类型匹配的情况又包含两种：一种是匹配普通的bean，一种是匹配自定义FactoryBean中的Bean类型
    3、假设匹配到的是自定义FactoryBean的factoryBeanName，那么就可以通过getBean(factoryBeanName)获取到通过自定义FactoryBean创建的bean对象，然后进行注入



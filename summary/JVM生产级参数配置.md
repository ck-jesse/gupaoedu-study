```text
JAVA_OPTIONS: 
-XX:ActiveProcessorCount=$MY_CPU_LIMIT 
-XX:MaxRAMPercentage=75 
-XX:InitialRAMPercentage=25 
-XX:MinRAMPercentage=25
```

```text
-XX:ActiveProcessorCount=$MY_CPU_LIMIT -XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=25 -XX:MinRAMPercentage=25
```

> JDK8U191为适配Docker容器新增的几个参数
>
> Docker容器模式下，我们可以给每个JVM实例所属的POD分配任意大小的内存上限。
>
> 比如，给每个账户服务分配4G，给每个支付服务分配8G。
>
> 如此一来，启动脚本就不好写成通用的了，指定3G也不是，指定6G也不是。
> 
> 但是，有了这三个新增参数，我们就可以在通用的启动脚本中指定75%（-XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=75 -XX:MinRAMPercentage=75）。
> 
> 那么，账户服务就相当于设置了-Xmx3g -Xms3g。而支付服务相当于设置了-Xmx6g -Xms6g 。


```text
POD 内存6G
-XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=75 -XX:MinRAMPercentage=75
-XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m
等价于：
-Xmx4608m -Xms4608m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m
```

> 注：+表示启用，-表示关闭
> 
> 关键系统的JVM参数推荐  https://www.cnblogs.com/kaleidoscope/p/9698732.html

```text
-XX:+PrintCommandLineFlags -XX:-OmitStackTraceInFastThrow -XX:-UseBiasedLocking -XX:-UseCounterDecay -XX:AutoBoxCacheMax=20000 -XX:+PerfDisableSharedMem  -Djava.net.preferIPv4Stack=true -XX:-TieredCompilation -server -Xms6144m -Xmx6144m -Xss512K -XX:MaxDirectMemorySize=2048m -XX:+AlwaysPreTouch -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m -XX:ReservedCodeCacheSize=240M -XX:+PrintPromotionFailure -XX:NewRatio=1 -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:MaxTenuringThreshold=4  -XX:+UnlockDiagnosticVMOptions -XX:ParGCCardsPerStrideChunk=1024 -XX:+ParallelRefProcEnabled -XX:+ExplicitGCInvokesConcurrent -XX:+CMSParallelInitialMarkEnabled
```

JAVA_OPTS:
-XX:+PrintCommandLineFlags  // 打印出启动参数行
-XX:-OmitStackTraceInFastThrow  // 针对异常做的一个优化，称为fast throw，抛出这个异常非常快，不用额外分配内存，也不用爬栈。正好是需要知道哪里出问题的时候看不到stack trace了，不利于排查问题。
-XX:-UseBiasedLocking   // 关闭偏向锁，JDK1.6开始默认打开的偏向锁，会尝试把锁赋给第一个访问它的线程
-XX:-UseCounterDecay    // 禁止JIT调用计数器衰减。默认情况下，每次GC时会对调用计数器进行砍半的操作，导致有些方法一直温热，永远都达不到触发C2编译的1万次的阀值。
-XX:AutoBoxCacheMax=20000   // 加大Integer Cache。Integer i=3;这语句有着 int自动装箱成Integer的过程，JDK默认只缓存 -128 ~ +127的Integer 和 Long，超出范围的数字就要即时构建新的Integer对象。设为20000后，我们应用的QPS有足足4%的提升。为什么是2万呢，因为-XX:+AggressiveOpts里也是这个值。
-XX:+PerfDisableSharedMem   // Cassandra家的一个参数，一直没留意，直到发生高IO时的JVM停顿。原来JVM经常会默默的在/tmp/hperf 目录写上一点statistics数据，如果刚好遇到PageCache刷盘，把文件阻塞了，就不能结束这个Stop the World的安全点了。
-XX:-TieredCompilation  // 多层编译是JDK8后默认打开的比较骄傲的功能，先以C1静态编译，采样足够后C2编译。
-XX:+AlwaysPreTouch // 启动时就把参数里说好了的内存全部舔一遍，可能令得启动时慢上一点，但后面访问时会更流畅，比如页面会连续分配，比如不会在晋升老生代时才去访问页面使得GC停顿时间加长。ElasticSearch和Cassandra都打开了它。
-Djava.net.preferIPv4Stack=true
-XX:MaxDirectMemorySize=104m    // (直接内存)堆外内存的最大值，默认为Heap区总内存减去一个Survivor区的大小，详见《Netty之堆外内存扫盲篇》，如果肯定用不了这么多，也可以把它主动设小，来获得一个比较清晰内存占用预估值，特别是在容器里。
-XX:MetaspaceSize=256m  // 最小元空间大小
-XX:MaxMetaspaceSize=512m   // 最大元空间大小
-XX:ReservedCodeCacheSize=240M  // JIT编译后二进制代码的存放区，满了之后就不再编译，对性能影响很大。 JDK7默认不开多层编译48M，开了96M，而JDK8默认开多层编译240M。可以在JMX里看看CodeCache的占用情况，也可以用VJTools里的vjtop来看，JDK7下默认的48M可以设大点，不抠这么点。
-server -Xms4096m -Xmx4096m -Xss512K    // 设置堆内存大小，线程栈大小
-XX:+PrintPromotionFailure  // 打开了就知道是多大的新生代对象晋升到老生代失败从而引发Full GC时的。
-XX:NewRatio=1  // JDK默认新生代占堆大小的1/3， 个人喜欢把对半分， 因为增大新生代能减少GC的频率，如果老生代里没多少长期对象的话，占2/3通常太多了。可以用-Xmn 直接赋值(等于-XX:NewSize and -XX:MaxNewSize同值的缩写)，或把NewRatio设为1来对半分。
-XX:+UseConcMarkSweepGC // 启用CMS垃圾收集器。为了稳健，还是8G以下的堆还是CMS好了，G1现在虽然是默认了，但其实在小堆里的表现也没有比CMS好，还是JDK11的ZGC引人期待。
-XX:CMSInitiatingOccupancyFraction=80   // 通过JMX监控内存达到80%的状况，设置让它75%就开始跑了，早点开始也能减少Full GC等意外情况(概念重申，这种主动的CMS GC，和JVM的老生代、永久代、堆外内存完全不能分配内存了而强制Full GC是不同的概念)。
-XX:+UseCMSInitiatingOccupancyOnly  // 启动CMS占用，使CMSInitiatingOccupancyFraction生效，否则只被用来做开始的参考值，后来还是JVM自己算。
-XX:MaxTenuringThreshold=4  // 对象在Survivor区最多熬过多少次Young GC后晋升到年老代，JDK8里CMS 默认是6，其他如G1是15。Young GC是最大的应用停顿来源，而YGC后存活对象的多少又直接影响停顿的时间，所以如果清楚Young GC的执行频率和应用里大部分临时对象的最长生命周期，可以把它设的更短一点，让其实不是临时对象的新生代对象赶紧晋升到年老代，别呆着。
-XX:+UnlockDiagnosticVMOptions  // 
-XX:ParGCCardsPerStrideChunk=1024   // 影响YGC时扫描老生代的时间，默认值256太小了，但32K也未必对，需要自己试验。
-XX:+ParallelRefProcEnabled // 并行的处理Reference对象，如WeakReference，默认为false，除非在GC log里出现Reference处理时间较长的日志，否则效果不会很明显，但我们总是要JVM尽量的并行，所以设了也就设了。同理还有-XX:+CMSParallelInitialMarkEnabled，JDK8已默认开启，但小版本比较低的JDK7甚至不支持。
-XX:+CMSParallelInitialMarkEnabled  // 
-XX:+ExplicitGCInvokesConcurrent    // 让full gc时使用CMS算法，不是全程停顿，必选。 但像R大说的，System GC是保护机制（如堆外内存满时清理它的堆内引用对象），禁了system.gc
() 未必是好事，只要没用什么特别烂的类库，真有人调了总有调的原因，所以不应该加这个烂大街的参数。
-Djava.security.egd=file:/dev/./urandom // SecureRandom生成加速。Tomcat的SecureRandom显式使用SHA1PRNG算法时，初始因子默认从/dev/random读取会存在堵塞。额外效果是SecureRandom的默认算法也变成更合适的SHA1了。
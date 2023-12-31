# study-jvm

### JVM 参数
     -XX:+PrintGC 输出GC日志
     -XX:+PrintGCDetails 输出GC的详细日志
     -XX:+PrintGCTimeStamps 输出GC的时间戳（以基准时间的形式）
     -XX:+PrintGCDateStamps 输出GC的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）
     -XX:+PrintHeapAtGC 在进行GC的前后打印出堆的信息
     -Xloggc:../logs/gc.log 日志文件的输出路径


​     
      -XX:+PrintCommandLineFlags                  [0] // 打印出启动参数行
      -XX:+UseConcMarkSweepGC                     [1] // 参数指定使用CMS垃圾回收器；
      -XX:+UseCMSInitiatingOccupancyOnly          [2] // 参数指定CMS垃圾回收器在老年代达到80%的时候开始工作，如果不指定那么默认的值为92%；
      -XX:CMSInitiatingOccupancyFraction=80       [3] // 同[2]
      -XX:+CMSClassUnloadingEnabled               [4] // 开启永久带（jdk1.8以下版本）或元数据区（jdk1.8及其以上版本）收集，如果没有设置这个标志，一旦永久代或元数据区耗尽空间也会尝试进行垃圾回收，但是收集不会是并行的，而再一次进行Full GC；
      -XX:+UseParNewGC                            [5] // 使用cms时默认这个参数就是打开的，不需要配置，cms只回收老年代，年轻代只能配合Parallel New或Serial回收器；
      -XX:+CMSParallelRemarkEnabled               [6]
      -XX:+CMSScavengeBeforeRemark                [7] // 如果Remark阶段暂停时间太长，可以启用这个参数，在Remark执行之前，先做一次ygc，也就是Minor GC。
      -XX:+UseCMSCompactAtFullCollection          [8] // 同[9]
      -XX:CMSFullGCsBeforeCompaction=0            [9] // 针对cms垃圾回收器碎片做优化的，CMS是不会移动内存的， 运行时间长了，会产生很多内存碎片
      -XX:+CMSConcurrentMTEnabled                 [10]
      -XX:ConcGCThreads=4                         [11] 
      -XX:+ExplicitGCInvokesConcurrent            [12]
      -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses    [13]
      -XX:+CMSParallelInitialMarkEnabled          [14] // 为了加快此阶段处理速度，减少停顿时间，可以开启初始标记并行化。表示在上一次CMS并发GC执行过后，到底还要再执行多少次full GC才会做压缩。默认是0，也就是在默认配置下每次CMS GC顶不住了而要转入full GC的时候都会做压缩。 
     
      -XX:+PrintGCDetails                         [15]
      -XX:+PrintGCCause                           [16]
      -XX:+PrintGCTimeStamps                      [17]
      -XX:+PrintGCDateStamps                      [18]
      -Xloggc:../logs/gc.log                      [19]
      -XX:+HeapDumpOnOutOfMemoryError             [20]
      -XX:HeapDumpPath=../dump                    [21]
      
      0. [0]打印出启动参数行 
      1. [1]参数指定使用CMS垃圾回收器； 
      2. [2]、[3]参数指定CMS垃圾回收器在老年代达到80%的时候开始工作，如果不指定那么默认的值为92%； 
      3. [4] 开启永久带（jdk1.8以下版本）或元数据区（jdk1.8及其以上版本）收集，如果没有设置这个标志，一旦永久代或元数据区耗尽空间也会尝试进行垃圾回收，但是收集不会是并行的，而再一次进行Full GC； 
      4. [5] 使用cms时默认这个参数就是打开的，不需要配置，cms只回收老年代，年轻带只能配合Parallel New或Serial回收器； 
      5. [6] 减少Remark阶段暂停的时间，启用并行Remark，如果Remark阶段暂停时间长，可以启用这个参数 
      6. [7] 如果Remark阶段暂停时间太长，可以启用这个参数，在Remark执行之前，先做一次ygc。因为这个阶段，年轻带也是cms的gcroot，cms会扫描年轻带指向老年代对象的引用，如果年轻带有大量引用需要被扫描，会让Remark阶段耗时增加； 
      7. [8]、[9]两个参数是针对cms垃圾回收器碎片做优化的，CMS是不会移动内存的， 运行时间长了，会产生很多内存碎片， 导致没有一段连续区域可以存放大对象，出现”promotion failed”、”concurrent mode failure”, 导致fullgc，启用UseCMSCompactAtFullCollection 在FULL GC的时候， 对年老代的内存进行压缩。-XX:CMSFullGCsBeforeCompaction=0 则是代表多少次FGC后对老年代做压缩操作，默认值为0，代表每次都压缩, 把对象移动到内存的最左边，可能会影响性能,但是可以消除碎片； 
      106.641: [GC 106.641: [ParNew (promotion failed): 14784K->14784K(14784K), 0.0370328 secs]106.678: [CMS106.715: [CMS-concurrent-mark: 0.065/0.103 secs] [Times: user=0.17 sys=0.00, real=0.11 secs] 
      (concurrent mode failure): 41568K->27787K(49152K), 0.2128504 secs] 52402K->27787K(63936K), [CMS Perm : 2086K->2086K(12288K)], 0.2499776 secs] [Times: user=0.28 sys=0.00, real=0.25 secs] 
      8. [11]定义并发CMS过程运行时的线程数。比如value=4意味着CMS周期的所有阶段都以4个线程来执行。尽管更多的线程会加快并发CMS过程，但其也会带来额外的同步开销。因此，对于特定的应用程序，应该通过测试来判断增加CMS线程数是否真的能够带来性能的提升。如果未设置这个参数，JVM会根据并行收集器中的-XX:ParallelGCThreads参数的值来计算出默认的并行CMS线程数： 
      ParallelGCThreads = (ncpus <=8 ? ncpus : 8+(ncpus-8)*5/8) ，ncpus为cpu个数， 
      ConcGCThreads =(ParallelGCThreads + 3)/4 
      这个参数一般不要自己设置，使用默认就好，除非发现默认的参数有调整的必要； 
      9. [12]、[13]开启foreground CMS GC，CMS gc 有两种模式，background和foreground，正常的cms gc使用background模式，就是我们平时说的cms gc；当并发收集失败或者调用了System.gc()的时候，就会导致一次full gc，这个fullgc是不是cms回收，而是Serial单线程回收器，加入了参数[12]后，执行full gc的时候，就变成了CMS foreground gc，它是并行full gc，只会执行cms中stop the world阶段的操作，效率比单线程Serial full GC要高；需要注意的是它只会回收old，因为cms收集器是老年代收集器；而正常的Serial收集是包含整个堆的，加入了参数[13],代表永久带也会被cms收集； 
      10. [14] 开启初始标记过程中的并行化，进一步提升初始化标记效率; 
      11. [15]、[16]、[17]、[18] 、[19]是打印gc日志，其中[16]在jdk1.8之后无需设置 
      12. [20]、[21]则是内存溢出时dump堆
      
      注：
      CMS并发GC无关的，CMS收集算法只是清理老年代
      CMS是基于标记-清除算法的，只会将标记为存活的对象删除，并不会移动对象整理内存空间，所以会造成内存碎片

### JAVA内存区域

![1563246375290](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1563246375290.png)



### 什么是GC ROOT？

    一些被称为引用链（GC Roots）的对象作为起点，从这些节点开始向下搜索，搜索走过的路径被称为（Reference Chain)，
    当一个对象到 GC Roots 没有任何引用链相连时（即从 GC Roots 节点到该节点不可达），则证明该对象是不可用的。



### 哪些对象是GC ROOT？

    虚拟机栈（栈帧中的本地变量表）中引用的对象
    方法区中类静态属性引用的对象
    方法区中常量引用的对象
    本地方法栈中 JNI（即一般说的 Native 方法）引用的对象

### GC 垃圾回收
    Garbage Collection，释放垃圾占用的空间，防止内存泄露。

### GC 回收类型
    年轻代空间（包括Eden和 Survivor 区域）回收内存被称为 Minor GC；
    对老年代GC称为Major GC；（CMS收集算法只是清理老年代）
    而Full GC是对整个堆来说的。



### GC 算法
#### 1、标记 --- 清除算法

![1563246295777](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1563246295777.png)

    标记清除算法（Mark-Sweep）是最基础的一种垃圾回收算法，它分为2部分，先把内存区域中的这些对象进行标记，
    哪些属于可回收标记出来，然后把这些垃圾拎出来清理掉。就像上图一样，清理掉的垃圾就变成未使用的内存区域，等待被再次使用。
    缺点：
    回收以后，内存会被切成很多段（内存碎片），而我们开辟空间时需要的是连续的内存区域，这样就导致，我们本身还要很多内存，但却使用不了。

#### 2、复制算法

![1563246276854](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1563246276854.png)

    复制算法（Copying）是在标记清除算法上演化而来，解决标记清除算法的内存碎片问题。
    它将可用内存按容量划分为大小相等的两块，每次只使用其中的一块。
    当这一块的内存用完了，就将还存活着的对象复制到另外一块上面，然后再把已使用过的内存空间一次清理掉。
    保证了内存的连续可用，内存分配时也就不用考虑内存碎片等复杂情况，逻辑清晰，运行高效。
    缺点：
    只能使用一半的内存，代价太大。

#### 3、标记整理算法

![1563246257478](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1563246257478.png)

    标记整理算法（Mark-Compact）标记过程仍然与标记 --- 清除算法一样，但后续步骤不是直接对可回收对象进行清理，
    而是让所有存活的对象都向一端移动，再清理掉端边界以外的内存区域。
    标记整理算法一方面在标记-清除算法上做了升级，解决了内存碎片的问题，也规避了复制算法只能利用一半内存区域的弊端。
    看起来很美好，但从上图可以看到，它对内存变动更频繁，需要整理所有存活对象的引用地址，在效率上比复制算法要差很多。

#### 4、分代收集算法
    分代收集算法（Generational Collection）严格来说并不是一种思想或理论，而是融合上述3种基础的算法思想，
    而产生的针对不同情况所采用不同算法的一套组合拳。对象存活周期的不同将内存划分为几块。
    一般是把 Java 堆分为新生代和老年代，这样就可以根据各个年代的特点采用最适当的收集算法。
    在新生代中，每次垃圾收集时都发现有大批对象死去，只有少量存活，那就选用复制算法，只需要付出少量存活对象的复制成本就可以完成收集。
    而老年代中因为对象存活率高、没有额外空间对它进行分配担保，就必须使用标记-清理或者标记 --- 整理算法来进行回收。

### 内存模型与回收策略
    Java 堆（Java Heap）是JVM所管理的内存中最大的一块，堆又是垃圾收集器管理的主要区域。
    Java 堆主要分为2个区域-年轻代与老年代，其中年轻代又分 Eden 区和 Survivor 区，其中 Survivor 区又分 From 和 To 2个区。

![1563246202672](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1563246202672.png)


#### 1、Eden 区

    IBM 公司的专业研究表明，有将近98%的对象是朝生夕死，所以针对这一现状，大多数情况下，对象会在新生代 Eden 区中进行分配，
    当 Eden 区没有足够空间进行分配时，虚拟机会发起一次 Minor GC，Minor GC 相比 Major GC 更频繁，回收速度也更快。
    通过 Minor GC 之后，Eden 会被清空，Eden 区中绝大部分对象会被回收，而那些无需回收的存活对象，
    将会进到 Survivor 的 From 区（若 From 区不够，则直接进入 Old 区）。

#### 2、Survivor 区
    Survivor 区相当于是 Eden 区和 Old 区的一个缓冲，类似于我们交通灯中的黄灯。
    Survivor 又分为2个区，一个是 From 区，一个是 To 区。
    每次执行 Minor GC，会将 Eden 区和 From 存活的对象放到 Survivor 的 To 区（如果 To 区不够，则直接进入 Old 区）。

#### 3、Old 区
    老年代占据着2/3的堆内存空间，只有在 Major GC 的时候才会进行清理，每次 GC 都会触发“Stop-The-World”  STW。
    内存越大，STW 的时间也越长，所以内存也不仅仅是越大就越好。
    由于复制算法在对象存活率较高的老年代会进行很多次的复制操作，效率很低，所以老年代这里采用的是标记 --- 整理算法。

#### 4、那些对象会进入Old 区
    【大对象】
    大对象指需要大量连续内存空间的对象，这部分对象不管是不是“朝生夕死”，都会直接进到老年代。
    这样做主要是为了避免在 Eden 区及2个 Survivor 区之间发生大量的内存复制。当你的系统有非常多“朝生夕死”的大对象时，得注意了。
    
    【长期存活对象】
    虚拟机给每个对象定义了一个对象年龄（Age）计数器。
    正常情况下对象会不断的在 Survivor 的 From 区与 To 区之间移动，对象在 Survivor 区中每经历一次 Minor GC，年龄就增加1岁。
    当年龄增加到15岁时，这时候就会被转移到老年代。当然，这里的15，JVM 也支持进行特殊设置。
    
    【动态对象年龄】
    虚拟机并不重视要求对象年龄必须到15岁，才会放入老年区，如果 Survivor 空间中相同年龄所有对象大小的总合大于 Survivor 空间的一半，年龄大于等于该年龄的对象就可以直接进去老年区，无需等你“成年”。
    这其实有点类似于负载均衡，轮询是负载均衡的一种，保证每台机器都分得同样的请求。看似很均衡，但每台机的硬件不通，健康状况不同，我们还可以基于每台机接受的请求数，或每台机的响应时间等，来调整我们的负载均衡算法。

### GC 垃圾收集器
    垃圾收集器就是内存回收的具体实现，为收集算法的内存回收方法提供具体实现。

#### 串行收集器
    串行收集器是最基本的历史最悠久的收集器，是一个单一线程的收集器意思是：
    当它在工作时，进行垃圾收集时其他所有工作线程必须暂停服务知道它收集结束有个优点是：当为他专门开一个线程来进行垃圾收集的时候，可以获得最高的单线程收集效率。

#### PARNER收集器
    ParNew收集器，是串行收集器的多线程版本，使用了多条线程进行垃圾收集而且可以进行参数控制（-XX：SurvivorRatio / -XX：PretenureSizeThreshold等），
    但是收集算法，对象分配规则，回收策略等都与串行相同。

#### Parallel Scavenge收集器
    是一个新生代收集器，也是使用复制算法的收集器。又是并行的多线程收集器。特点是：可控制的吞吐量：吞吐量=运行用户代码时间/（运行用户代码时间+垃圾收集时间）。高吞吐量以为着高效的利用了CPU时间。

#### Serial Old收集器
    它是Parallel Scavenge收集器的老版年代本，使用多线程和“标记 - 整理”算法。

#### CMS收集器（并发标记清除）
    CMS（Concurrent Mark Sweep）并发收集器
    C :  Concurrent
    M :  标记（marking）对象 ：GC必须记住哪些对象可达，以便删除不可达的对象 
    S :  清除（sweeping） ： 删除未标记的对象并释放它们的内存
    
    CMS是一种以最短停顿时间为目标的收集器，使用CMS并不能达到GC效率最高，但它尽可能降低GC时服务的停顿时间。
    使用标记-清除算法（Mark Sweep），在运行时会产生内存碎片
    虚拟机提供了参数开启CMS收集结束后再进行一次内存压缩。
    设置在垃圾收集器后是否需要一次内存碎片整理过程，仅在CMS收集器时有效
    -XX:+CMSFullGCBeforeCompaction
    设置CMS收集器在进行若干次垃圾收集后再进行一次内存碎片整理过程
    通常与 UseCMSCompactAtFullCollection 参数一起使用

#### G1收集器
    它是目前收集器技术发展最前沿的成果之一：
    并行与并发
    分代收集
    空间整合
    可预测的停顿


​    
#### GC日志
    通过日志内容也可以得到GC相关的信息。因为GC日志模块内置于JVM中, 所以日志中包含了对GC活动最全面的描述。 这就是事实上的标准, 可作为GC性能评估和优化的最真实数据来源。
    GCViewer 是一款开源的GC日志分析工具。

### GC 调优
    # jps 查看正在运行的Java进程列表
    jps
    18485 credit-mobile-access-deploy.jar
    
    # 【jstat 对于快速确定GC行为是否健康非常有用】
    # jstat 每秒向标准输出输出一行新内容
    jstat -gc -t 18485 1s
    ---------------------------------
    Timestamp  S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT   
    200.0    8448.0 8448.0 8448.0  0.0   67712.0  67712.0   169344.0   169344.0  21248.0 20534.3 3072.0 2807.7     34    0.720  658   133.684  134.404
    201.0    8448.0 8448.0 8448.0  0.0   67712.0  67712.0   169344.0   169343.2  21248.0 20534.3 3072.0 2807.7     34    0.720  662   134.712  135.432
    202.0    8448.0 8448.0 8102.5  0.0   67712.0  67598.5   169344.0   169343.6  21248.0 20534.3 3072.0 2807.7     34    0.720  667   135.840  136.559
    203.0    8448.0 8448.0 8126.3  0.0   67712.0  67702.2   169344.0   169343.6  21248.0 20547.2 3072.0 2807.7     34    0.720  669   136.178  136.898
    
    分析：
    1、jstat 连接到 JVM 的时间, 是JVM启动后的 200.1秒。此信息从第一行的 “Timestamp” 列得知。
    2、从第一行的 “YGC” 列得知年轻代共执行了34次GC, 由 “FGC” 列得知整个堆内存已经执行了 658次 full GC。
    3、年轻代的GC耗时总共为 0.720 秒, 显示在“YGCT” 这一列。
    4、Full GC 的总计耗时为 133.684 秒, 由“FGCT”列得知。 这立马就吸引了我们的目光, 总的JVM 运行时间有 200.1 秒, 但其中有 66% 的部分被 Full GC 消耗了
    5、在接下来的一秒内共执行了 4 次 Full GC。参见 “FGC” 列.
    6、这4次 Full GC 暂停占用了差不多 1秒的时间(根据 FGCT列的差得知)。与第一行相比, Full GC 耗费了928 毫秒, 即 92.8% 的时间。
    7、根据 “OC 和 “OU” 列得知, 整个老年代的空间为 169,344.0 KB (“OC“), 在 4 次 Full GC 后依然占用了 169,344.2 KB (“OU“)。用了 928ms 的时间却只释放了 800 字节的内存, 怎么看都觉得很不正常。
    
    只看这两行的内容, 就知道程序出了很严重的问题。继续分析下一行, 可以确定问题依然存在,而且变得更糟。


​    
    以上可以看出，通过 jstat 能很快发现对JVM健康极为不利的GC行为。一般来说, 只看 jstat 的输出就能快速发现以下问题:
    1、最后一列 “GCT”, 与JVM的总运行时间 “Timestamp” 的比值, 就是GC 的开销。如果每一秒内, “GCT” 的值都会明显增大, 与总运行时间相比, 就暴露出GC开销过大的问题. 不同系统对GC开销有不同的容忍度, 由性能需求决定, 一般来讲, 超过 10% 的GC开销都是有问题的。
    2、“YGC” 和 “FGC” 列的快速变化往往也是有问题的征兆。频繁的GC暂停会累积,并导致更多的线程停顿(stop-the-world pauses), 进而影响吞吐量。
    3、如果看到 “OU” 列中,老年代的使用量约等于老年代的最大容量(OC), 并且不降低的话, 就表示虽然执行了老年代GC, 但基本上属于无效GC。


​    
    问题分析： Concurrent Mode Failure
    执行 CMS GC 的过程中同时有对象要放入老年代，而此时老年代空间不足（有时候“空间不足”是 CMS GC 时当前的浮动垃圾过多导致暂时性的空间不足触发 Full GC），便会报 Concurrent Mode Failure 错误，并触发 Full GC。

#### GC优化步骤
    GC优化一般步骤可以概括为：确定目标、优化参数、验收结果。
    通过收集GC信息，结合系统需求，确定优化方案，例如选用合适的GC回收器、重新设置内存比例、调整JVM参数等。

#### 案例一：Major GC和Minor GC频繁
    首先优化Minor GC频繁问题。通常情况下，由于新生代空间较小，Eden区很快被填满，就会导致频繁Minor GC，因此可以通过增大新生代空间来降低Minor GC的频率。
    如何选择各分区大小应该依赖应用程序中对象生命周期的分布情况：如果应用存在大量的短期对象，应该选择较大的年轻代；如果存在相对较多的持久对象，老年代应该适当增大。

#### 案例二：请求高峰期发生GC，导致服务可用性下降
    GC日志显示，高峰期CMS在重标记（Remark）阶段耗时1.39s。Remark阶段是Stop-The-World（以下简称为STW）的，
    即在执行垃圾回收时，Java应用程序中除了垃圾回收器线程之外其他所有线程都被挂起，意味着在此期间，用户正常工作的线程全部被暂停下来，这是低延时服务不能接受的。本次优化目标是降低Remark时间。
    
    CMS的四个主要阶段，以及各个阶段的工作内容：
    1、Init-mark初始标记(STW) ，该阶段进行可达性分析，标记GC ROOT能直接关联到的对象，所以很快。
    2、Concurrent-mark并发标记，由前阶段标记过的绿色对象出发，所有可到达的对象都在本阶段中标记。
    3、Remark重标记(STW) ，暂停所有用户线程，重新扫描堆中的对象，进行可达性分析，标记活着的对象。因为并发标记阶段是和用户线程并发执行的过程，所以该过程中可能有用户线程修改某些活跃对象的字段，指向了一个未标记过的对象，如下图中红色对象在并发标记开始时不可达，但是并行期间引用发生变化，变为对象可达，这个阶段需要重新标记出此类对象，防止在下一阶段被清理掉，这个过程也是需要STW的。特别需要注意一点，这个阶段是以新生代中对象为根来判断对象是否存活的。
    4、并发清理，进行并发的垃圾清理。
    
    通过案例分析了解到，由于跨代引用的存在，CMS在Remark阶段必须扫描整个堆，同时为了避免扫描时新生代有很多对象，增加了可中断的预清理阶段用来等待Minor GC的发生。
    只是该阶段有时间限制，如果超时等不到Minor GC，Remark时新生代仍然有很多对象，我们的调优策略是，通过参数强制Remark前进行一次Minor GC，从而降低Remark阶段的时间。
    
    总结来说，CMS的设计聚焦在获取最短的时延，为此它“不遗余力”地做了很多工作，包括尽量让应用程序和GC线程并发、增加可中断的并发预清理阶段、引入卡表等，
    虽然这些操作牺牲了一定吞吐量但获得了更短的回收停顿时间。
    
    结合上述GC优化案例做个总结：
    首先再次声明，在进行GC优化之前，需要确认项目的架构和代码等已经没有优化空间。我们不能指望一个系统架构有缺陷或者代码层次优化没有穷尽的应用，通过GC优化令其性能达到一个质的飞跃。
    其次，通过上述分析，可以看出虚拟机内部已有很多优化来保证应用的稳定运行，所以不要为了调优而调优，不当的调优可能适得其反。
    最后，GC优化是一个系统而复杂的工作，没有万能的调优策略可以满足所有的性能指标。GC优化必须建立在我们深入理解各种垃圾回收器的基础上，才能有事半功倍的效果。


### 常见配置汇总
    1、堆设置
    -Xms :初始堆大小
    -Xmx :最大堆大小
    -XX:NewSize=n :设置年轻代大小
    -XX:NewRatio=n: 设置年轻代和年老代的比值。如:为3，表示年轻代与年老代比值为 1：3，年轻代占整个年轻代年老代和的1/4
    -XX:SurvivorRatio=n :年轻代中Eden区与两个Survivor区的比值。注 意Survivor区有两个。如：3，表示Eden：Survivor=3：2，一个Survivor区占整个年轻代的1/5
    -XX:MaxPermSize=n :设置持久代大小
    2、收集器设置
    -XX:+UseSerialGC :设置串行收集器
    -XX:+UseParallelGC :设置并行收集器
    -XX:+UseParalledlOldGC :设置并行年老代收集器
    -XX:+UseConcMarkSweepGC :设置CMS并发收集器
    -XX:+UseG1GC :设置G1收集器
    3、垃圾回收统计信息
    -XX:+PrintGC
    -XX:+PrintGCDetails
    -XX:+PrintGCTimeStamps
    -Xloggc:filename
    4、并行收集器设置
    -XX:ParallelGCThreads=n :设置并行收集器收集时使用的CPU数。并行收集线程数。
    -XX:MaxGCPauseMillis=n :设置并行收集最大暂停时间
    -XX:GCTimeRatio=n :设置垃圾回收时间占程序运行时间的百分比。公式为 1/(1+n)
    5、并发收集器设置
    -XX:+CMSIncrementalMode :设置为增量模式。适用于单CPU情况。
    -XX:ParallelGCThreads=n :设置并发收集器年轻代收集方式为并行收集时，使 用的CPU数。并行收集线程数。


### gcviewer - 本地查看GC日志文件工具
> 方便查看 吞吐量 和 停顿时间
> 
> gceasy.io - 在线查看GC日志



### JVM调优  - demo

> 1、设置JVM参数，打印出来gc日志
>
> 2、通过gcviewer工具分析gc日志文件
>
> 3、关注gcviewer中的指标：
>
> ​	Event details - GC次数
>
> ​	Summary - Throughput 吞吐量
>
> ​	Pause - Min / Max Pause 最大最小停顿时间；Avg Pause 平均停顿时间
>
> 4、通过上面几个指标，来调整JVM参数，以达到最优。



特别注意：不同垃圾收集器，其特性不同，调优的参数也不同。

CMS

> 针对老年代
>
> 标记清除 - 空间碎片

G1

> 针对新生代和老年代
>
> 标记整理 - 
>
> 优先收集垃圾比较多的区域region
>
> G1使用的条件：多核，大内存（堆最小4G、6G）

```
# 设置为G1收集器，并设置GC最大停顿时间为15ms
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseG1GC -XX:MaxGCPauseMillis=15 -Xloggc:g1_gc.log

# 设置为CMS收集器，
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+UseConcMarkSweepGC  -Xloggc:cms_gc.log
```





### 高并发场景下的JVM优化



### JVM性能优化





内存泄漏 - memory leak

> 垃圾对象，一直没有被回收，导致一直内存泄漏
>
> 是指程序在申请内存后，无法释放已申请的内存空间就造成了内存泄漏，一次内存泄漏似乎不会有大的影响，但内存泄漏堆积后的后果就是内存溢出。

内存溢出 - out of memory

> OOM，指程序申请内存时，没有足够的内存供申请者使用。


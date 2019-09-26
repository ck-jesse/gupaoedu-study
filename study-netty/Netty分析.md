## Netty
参见博客 ： https://www.cnblogs.com/imstudy/p/9908791.html
Netty 是一个广受欢迎的异步事件驱动的Java开源网络应用程序框架，用于快速开发可维护的高性能协议服务器和客户端。

## JDK 原生 NIO 程序的问题
    JDK 原生也有一套网络应用程序 API，但是存在一系列问题，主要如下：
    1）NIO 的类库和 API 繁杂，使用麻烦：你需要熟练掌握 Selector、ServerSocketChannel、SocketChannel、ByteBuffer 等。
    2）需要具备其他的额外技能做铺垫：例如熟悉 Java 多线程编程，因为 NIO 编程涉及到 Reactor 模式，你必须对多线程和网路编程非常熟悉，才能编写出高质量的 NIO 程序。
    3）可靠性能力补齐，开发工作量和难度都非常大：例如客户端面临断连重连、网络闪断、半包读写、失败缓存、网络拥塞和异常码流的处理等等。NIO 编程的特点是功能开发相对容易，但是可靠性能力补齐工作量和难度都非常大。
    4）JDK NIO 的 Bug：例如臭名昭著的 Epoll Bug，它会导致 Selector 空轮询，最终导致 CPU 100%。
    官方声称在 JDK 1.6 版本的 update 18 修复了该问题，但是直到 JDK 1.7 版本该问题仍旧存在，只不过该 Bug 发生概率降低了一些而已，它并没有被根本解决。

## Netty 的特点
    Netty 对 JDK 自带的 NIO 的 API 进行了封装，解决了上述问题。
    Netty的主要特点有：
    1）设计优雅：适用于各种传输类型的统一 API 阻塞和非阻塞 Socket；基于灵活且可扩展的事件模型，可以清晰地分离关注点；
    高度可定制的线程模型 - 单线程，一个或多个线程池；真正的无连接数据报套接字支持（自 3.1 起）。
    2）使用方便：详细记录的 Javadoc，用户指南和示例；没有其他依赖项，JDK 5（Netty 3.x）或 6（Netty 4.x）就足够了。
    3）高性能、吞吐量更高：延迟更低；减少资源消耗；最小化不必要的内存复制。
    4）安全：完整的 SSL/TLS 和 StartTLS 支持。
    5）社区活跃、不断更新：社区活跃，版本迭代周期短，发现的 Bug 可以被及时修复，同时，更多的新功能会被加入。

## Netty 常见使用场景
    Netty 常见的使用场景如下：
    1）互联网行业：在分布式系统中，各个节点之间需要远程服务调用，高性能的 RPC 框架必不可少，Netty 作为异步高性能的通信框架，
    往往作为基础通信组件被这些 RPC 框架使用。典型的应用有：阿里分布式服务框架 Dubbo 的 RPC 框架使用 Dubbo 协议进行节点间通信，
    Dubbo 协议默认使用 Netty 作为基础通信组件，用于实现各进程节点之间的内部通信。
    2）游戏行业：无论是手游服务端还是大型的网络游戏，Java 语言得到了越来越广泛的应用。Netty 作为高性能的基础通信组件，
    它本身提供了 TCP/UDP 和 HTTP 协议栈。
    非常方便定制和开发私有协议栈，账号登录服务器，地图服务器之间可以方便的通过 Netty 进行高性能的通信。
    3）大数据领域：经典的 Hadoop 的高性能通信和序列化组件 Avro 的 RPC 框架，默认采用 Netty 进行跨界点通信，
    它的 Netty Service 基于 Netty 框架二次封装实现。
    有兴趣的读者可以了解一下目前有哪些开源项目使用了 Netty的Related Projects。

## Netty 高性能设计
    Netty 作为异步事件驱动的网络，高性能之处主要来自于其 I/O 模型和线程处理模型，前者决定如何收发数据，后者决定如何处理数据。



### Netty Reactor 模型

>  服务端 Netty 的工作架构图：

![img](https://upload-images.jianshu.io/upload_images/1500839-55f5b1d5ddc13581.jpg)

> 分析：
>
> Server 端包含 1 个 Boss NioEventLoopGroup 和 1 个 Worker NioEventLoopGroup。
>
> NioEventLoopGroup 相当于 1 个事件循环组，这个组里包含多个事件循环 NioEventLoop，每个 NioEventLoop 包含 1 个 Selector 和 1 个事件循环线程。
>
> 每个 Boss NioEventLoop 循环执行的任务包含 3 步：
>
> 1）轮询 Accept 事件；
>
> 2）处理 Accept I/O 事件，与 Client 建立连接，生成 NioSocketChannel，并将 NioSocketChannel 注册到某个 Worker NioEventLoop 的 Selector 上；
>
> 3）处理任务队列中的任务，runAllTasks。任务队列中的任务包括用户调用 eventloop.execute 或 schedule 执行的任务，或者其他线程提交到该 eventloop 的任务。
>
> 每个 Worker NioEventLoop 循环执行的任务包含 3 步：
>
> 1）轮询 Read、Write 事件；
>
> 2）处理 I/O 事件，即 Read、Write 事件，在 NioSocketChannel 可读、可写事件发生时进行处理；
>
> 3）处理任务队列中的任务，runAllTasks。





## 2、线程模型

详细介绍参见： http://www.52im.net/thread-1939-1-1.html



### 2.1 模型1：传统阻塞 I/O 服务模型

> **特点：**
>
> - 1）采用阻塞式 I/O 模型获取输入数据；
> - 2）每个连接都需要独立的线程完成数据输入，业务处理，数据返回的完整操作。
>
> **存在问题：**
>
> - 1）当并发数较大时，需要创建大量线程来处理连接，系统资源占用较大；
> - 2）连接建立后，如果当前线程暂时没有数据可读，则线程就阻塞在 Read 操作上，造成线程资源浪费。



### 2.2 模型2：Reactor 线程模型

#### 2.2.1 基本介绍

> **针对传统阻塞 I/O 服务模型的 2 个缺点，比较常见的有如下解决方案：**
>
> 1）基于 I/O 复用模型：多个连接共用一个阻塞对象，应用程序只需要在一个阻塞对象上等待，无需阻塞等待所有连接。当某条连接有新的数据可以处理时，操作系统通知应用程序，线程从阻塞状态返回，开始进行业务处理；
>
> 2）基于线程池复用线程资源：不必再为每个连接创建线程，将连接完成后的业务处理任务分配给线程进行处理，一个线程可以处理多个连接的业务。
>
> **I/O 复用结合线程池，这就是 Reactor 模式基本设计思想，如下图：**



![img](http://www.52im.net/data/attachment/forum/201809/06/195839s5hi3te5pxueq5ze.jpeg)



> Reactor 是反应堆的意思，Reactor 模型是指通过一个或多个输入同时传递给服务处理器的服务请求的事件驱动处理模式。
> 服务端程序处理传入多路请求，并将它们同步分派给请求对应的处理线程，Reactor 模式也叫 Dispatcher 模式，即 I/O 多了复用统一监听事件，收到事件后分发(Dispatch 给某进程)，是编写高性能网络服务器的必备技术之一。 



#### 2.2.2 Reactor 模型中有 2 个关键组成：

> 1）Reactor：Reactor 在一个单独的线程中运行，负责监听和分发事件，分发给适当的处理程序来对 IO 事件做出反应。
> 它就像公司的电话接线员，它接听来自客户的电话并将线路转移到适当的联系人；
> 2）Handlers：处理程序执行 I/O 事件要完成的实际事件，类似于客户想要与之交谈的公司中的实际官员。
> Reactor 通过调度适当的处理程序来响应 I/O 事件，处理程序执行非阻塞操作。



#### **2.2.3 根据 Reactor 的数量和处理资源池线程的数量不同，有 3 种典型的实现：**

> - 1）单 Reactor 单线程；
> - 2）单 Reactor 多线程；
> - 3）主从 Reactor 多线程。



#### 2.2.4 单 Reactor 单线程



![img](http://www.52im.net/data/attachment/forum/201809/06/200048bgll2l41w72174ot.jpeg)

> 其中，Select 是前面 I/O 复用模型介绍的标准网络编程 API，可以实现应用程序通过一个阻塞对象监听多路连接请求，其他方案示意图类似。
> **方案说明：**
>
> - 1）Reactor 对象通过 Select 监控客户端请求事件，收到事件后通过 Dispatch 进行分发；
> - 2）如果是建立连接请求事件，则由 Acceptor 通过 Accept 处理连接请求，然后创建一个 Handler 对象处理连接完成后的后续业务处理；
> - 3）如果不是建立连接事件，则 Reactor 会分发调用连接对应的 Handler 来响应；
> - 4）Handler 会完成 Read→业务处理→Send 的完整业务流程。
>
> 优点：
>
> 模型简单，没有多线程、进程通信、竞争的问题，全部都在一个线程中完成。
>
> 缺点：
> 可靠性问题，线程意外跑飞，或者进入死循环，会导致整个系统通信模块不可用，不能接收和处理外部消息，造成节点故障。
> 客户端的数量有限，业务处理非常快速，比如 Redis，业务处理的时间复杂度 O(1)。
>
> 使用场景：
>
> 客户端的数量有限，业务处理非常快速，比如 Redis，业务处理的时间复杂度 O(1)。



#### 2.2.5 单 Reactor 多线程

![img](http://www.52im.net/data/attachment/forum/201809/06/200650wun9j9ghkgk7ngna.jpeg)



> 方案说明：
>
> - 1）Reactor 对象通过 Select 监控客户端请求事件，收到事件后通过 Dispatch 进行分发；
> - 2）如果是建立连接请求事件，则由 Acceptor 通过 Accept 处理连接请求，然后创建一个 Handler 对象处理连接完成后续的各种事件；
> - 3）如果不是建立连接事件，则 Reactor 会分发调用连接对应的 Handler 来响应；
> - 4）Handler 只负责响应事件，不做具体业务处理，通过 Read 读取数据后，会分发给后面的 Worker 线程池进行业务处理；
> - 5）Worker 线程池会分配独立的线程完成真正的业务处理，如何将响应结果发给 Handler 进行处理；
> - 6）Handler 收到响应结果后通过 Send 将响应结果返回给 Client。
>
> 优点：
>
> 可以充分利用多核 CPU 的处理能力。
>
> 缺点：
>
> 多线程数据共享和访问比较复杂；Reactor 承担所有事件的监听和响应，在单线程中运行，高并发场景下容易成为性能瓶颈。



#### 2.2.6 主从 Reactor 多线程

![img](http://www.52im.net/data/attachment/forum/201809/06/200759gg777fr7v7wzcr7r.jpeg)



> 针对单 Reactor 多线程模型中，Reactor 在单线程中运行，高并发场景下容易成为性能瓶颈，可以让 Reactor 在多线程中运行。
>
> 方案说明：
>
> - 1）Reactor 主线程 MainReactor 对象通过 Select 监控建立连接事件，收到事件后通过 Acceptor 接收，处理建立连接事件；
>
> - 2）Acceptor 处理建立连接事件后，MainReactor 将连接分配 Reactor 子线程给 SubReactor 进行处理；
>
> - 3）SubReactor 将连接加入连接队列进行监听，并创建一个 Handler 用于处理各种连接事件；
>
> - 4）当有新的事件发生时，SubReactor 会调用连接对应的 Handler 进行响应；
>
> - 5）Handler 通过 Read 读取数据后，会分发给后面的 Worker 线程池进行业务处理；
>
> - 6）Worker 线程池会分配独立的线程完成真正的业务处理，如何将响应结果发给 Handler 进行处理；
>
> - 7）Handler 收到响应结果后通过 Send 将响应结果返回给 Client。
>
> 优点：
>
> 父线程与子线程的数据交互简单职责明确，父线程只需要接收新连接，子线程完成后续的业务处理。
>
> 父线程与子线程的数据交互简单，Reactor 主线程只需要把新连接传给子线程，子线程无需返回数据。
>
> 这种模型在许多项目中广泛使用，包括 Nginx 主从 Reactor 多进程模型，Memcached 主从多线程，Netty 主从多线程模型的支持。
>
> 缺点：
>
> 

#### 小结

>**3 种模式可以用个比喻来理解：**（餐厅常常雇佣接待员负责迎接顾客，当顾客入坐后，侍应生专门为这张桌子服务）
>
>- 1）单 Reactor 单线程，接待员和侍应生是同一个人，全程为顾客服务；
>- 2）单 Reactor 多线程，1 个接待员，多个侍应生，接待员只负责接待；
>- 3）主从 Reactor 多线程，多个接待员，多个侍应生。
>
>**Reactor 模式具有如下的优点：**
>
>1）响应快，不必为单个同步时间所阻塞，虽然 Reactor 本身依然是同步的；
>
>2）编程相对简单，可以最大程度的避免复杂的多线程及同步问题，并且避免了多线程/进程的切换开销；
>
>3）可扩展性，可以方便的通过增加 Reactor 实例个数来充分利用 CPU 资源；
>
>4）可复用性，Reactor 模型本身与具体事件处理逻辑无关，具有很高的复用性。



### 2.3 模型2：Proactor 模型

>   在 Reactor 模式中，Reactor 等待某个事件或者可应用或者操作的状态发生（比如文件描述符可读写，或者是 Socket 可读写）。
>
> 然后把这个事件传给事先注册的 Handler（事件处理函数或者回调函数），由后者来做实际的读写操作。
>
> 其中的读写操作都需要应用程序同步操作，所以 Reactor 是非阻塞同步网络模型。
>
> 如果把 I/O 操作改为异步，即交给操作系统来完成就能进一步提升性能，这就是异步网络模型 Proactor。  

![img](http://www.52im.net/data/attachment/forum/201809/06/201251i0om3mro9wtcxrty.jpeg)



#### **2.3.1 Proactor 是和异步 I/O 相关的，详细方案如下：**

>- 1）Proactor Initiator 创建 Proactor 和 Handler 对象，并将 Proactor 和 Handler 都通过 AsyOptProcessor（Asynchronous Operation Processor）注册到内核；
>- 2）AsyOptProcessor 处理注册请求，并处理 I/O 操作；
>- 3）AsyOptProcessor 完成 I/O 操作后通知 Proactor；
>- 4）Proactor 根据不同的事件类型回调不同的 Handler 进行业务处理；
>- 5）Handler 完成业务处理。



#### 2.3.2 Proactor 和 Reactor 的区别：

> 1）Reactor 是在事件发生时就通知事先注册的事件（读写在应用程序线程中处理完成）；
>
> 2）Proactor 是在事件发生时基于异步 I/O 完成读写操作（由内核完成），待 I/O 操作完成后才回调应用程序的处理器来进行业务处理。

#### 2.3.3 Proactor有如下缺点

> - 1）编程复杂性，由于异步操作流程的事件的初始化和事件完成在时间和空间上都是相互分离的，因此开发异步应用程序更加复杂。应用程序还可能因为反向的流控而变得更加难以 Debug；
> - 2）内存使用，缓冲区在读或写操作的时间段内必须保持住，可能造成持续的不确定性，并且每个并发操作都要求有独立的缓存，相比 Reactor 模式，在 Socket 已经准备好读或写前，是不要求开辟缓存的；
> - 3）操作系统支持，Windows 下通过 IOCP 实现了真正的异步 I/O，而在 Linux 系统下，Linux 2.6 才引入，目前异步 I/O 还不完善。

因此在 Linux 下实现高并发网络编程都是以 Reactor 模型为主。
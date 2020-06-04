# zookeeper 原理
    节点类型：
    持久节点，
    持久有序节点
    临时节点，
    临时有序节点

### 分布式锁的实现原理
    1、创建一个临时有序节点（此时未持有锁）
    方法：org.apache.curator.framework.recipes.locks.LockInternalsDriver#createsTheLock
    2、自旋（死循环）判断当前节点是否能够持有锁
    方法：org.apache.curator.framework.recipes.locks.LockInternals#internalLockLoop
    3、获取到所有子节点列表，并且从小到大根据节点名称后10位数字进行排序；
    若能持有锁，则返回true；
    若不能持有锁，则获取到上一个临时节点的path，并添加一个watcher监听（也就是当前线程会监听自己节点的上一个节点的变化，而不是监听父节点下所有节点的变动）；
    然后当前线程进入wait，等待被唤醒；
    当监听的节点发生了变动时，那么就将线程从等待状态唤醒，重新开始下一轮的锁的争抢。
    总结：
    分布式锁的本质就是通过创建一个临时有序节点，和watcher监听上一个节点的变动来实现的。
    注意：利用临时有序节点+watcher监听机制可以做很多事情，如分布式锁、Leader选举、分布式队列（FIFO）
    
### 基于zookeeper实现服务的leader选举的原理
    1、创建一个线程池，然后将在执行 LeaderSelector.start()方法时,往线程池中提交一个workloop任务
    2、调用方法internalRequeue，实质为执行任务对应方法 LeaderSelector.doWork()，其中使用了分布式锁InterProcessMutex.acquire()来无限等待获得锁
    3、若当前节点获得锁，则执行 LeaderSelectorListener.takeLeadership()方法，也就是自定义的Listener中的方法
    当该方法持有执行权限时，则表示当前节点被选举为leader了，退出该方法时，则表示放弃执行权限，放弃领导权
    4、若当前节点未获得锁，则无限等待获得锁（实质就是利用了分布式锁的特性来控制）
    5、当放弃领导权时，并且LeaderSelector.autoRequeue()为true，则再次调用internalRequeue来获取锁（获取leader）
    总结：
    leader选举的本质就是利用的分布式锁的特性来实现的主从选举。watcher监听上一个临时有序节点，然后无限等待，直到获得锁，获得锁则表示被选举为了leader。

### zookeeper集群自身的leader选举的实现原理（zab协议）
    server1 （异步发送/接收票据的线程）（队列采用生产者消费者模式）
	sendQueue sendWorderThread 发送票据队列
	receQueue receWorkerThread 接收票据队列
	zab协议（决定当前节点是否为leader）
	leader选举（当前节点的票据与接收到的票据集合中的票据比较，若当前票据在集合中的数量过半则认为是leader，这也就是所谓的过半节点同意则选举为leader的说法）
	若

    server2

### watcher机制的实现原理
    客户端与服务端进行网络通信（NIO/Netty）
    1、建立与服务端的SocketChannel连接
    2、将watcher监听的path存放到queue中，
    3、等待服务端的通知，当服务端path
    客户端（异步生产者消费者模式来实现）
    SendThread 从queue取出watcher并发送到服务端
    EventThread 从queue取出服务端响应的watcher事件，并执行
    
    服务端
    1、接收客户端连接，并将连接保存到 Map<path,BlockingQueue<Watcher>> 中，Map中的Watcher实际为一个客户端连接。
    2、当watcher监听的path有变化时，则通知到监听该path的客户端
    watcherTable（异步生产者消费者模式来实现）
    责任链模式实现Processor
    提供Leader和Follower的Processor实现
    （要注意每一个Processor的下一个Processor是谁，这样才方便阅读源码）
 

## 源码分析

### 客户端与Zookeeper服务端通信

```text
org.apache.zookeeper.ClientCnxn
> 通信的核心类，包含 SendThread 和 EventThread

org.apache.zookeeper.ClientCnxn.start
> 启动SendThread.run()和EventThread.run()

org.apache.zookeeper.ClientCnxn.SendThread.run
> 自旋，

org.apache.zookeeper.ClientCnxn.SendThread.startConnect
> 建立NIO SocketChannel 连接

org.apache.zookeeper.ClientCnxnSocket.doTransport
> 发送数据
> pendingQueue 已经发送并等待响应的数据包
> outgoingQueue 存放需要发送的数据包

org.apache.zookeeper.ClientCnxnSocketNIO.doIO
> 对NIO读写事件进行处理
> 读事件处理：通过SendThread.readResponse读取并解析数据，并通过EventThread.queueEvent将事件存放到waitingEvents
> 写事件处理：outgoingQueue队列中有数据时，则取出来，并通过NIO的SocketChannel将数据发送到服务端。（通过synchronized保证同步）

org.apache.zookeeper.ClientCnxn.EventThread.queueEvent
> 将接收到的事件信息存放到待处理的事件队列waitingEvents

org.apache.zookeeper.ClientCnxn.EventThread.run
> 自旋，从waitingEvents中读取事件，并处理事件。

org.apache.zookeeper.ClientCnxn.EventThread.processEvent
> 事件处理
> WatcherSetEventPair: 观察者处理事件，实际为org.apache.zookeeper.Watcher.process事件处理
> ExistsResponse
> SetDataResponse
> SetACLResponse
> GetDataResponse
> GetACLResponse
> GetChildrenResponse
> GetChildren2Response
> CreateResponse
> MultiResponse
> VoidCallback

org.apache.zookeeper.AsyncCallback
> 异步回调，处理结果

org.apache.zookeeper.AsyncCallback.VoidCallback.processResult
> 不同的异步回调实现有各自的processResult
```
# zookeeper 原理

### zookeeper leader选举的实现原理（zab协议）
    server1 （异步发送/接收票据的线程）（队列采用生产者消费者模式）
	sendQueue sendWorderThread 发送票据队列
	receQueue receWorkerThread 接收票据队列
	zab协议（决定当前节点是否为leader）
	leader选举（当前节点的票据与接收到的票据集合中的票据比较，若当前票据在集合中的数量过半则认为是leader，这也就是所谓的过半节点同意则选举为leader的说法）
	若

    server2

### watcher机制的实现原理
    客户端与服务端进行网络通信（NIO/Netty）
    watcher存放到queue中
    客户端
    SendThread 从queue取出watcher并发送到服务端
    EventThread 从queue取出服务端响应的watcher事件，并执行
    
    服务端
    watcherTable
    责任链模式实现Processor
    提供Leader和Follower的Processor实现
    （要注意每一个Processor的下一个Processor是谁，这样才方便阅读源码）
 


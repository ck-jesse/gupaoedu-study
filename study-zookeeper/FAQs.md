# zookeeper 原理

### zookeeper leader选举的实现原理（zab协议）
    server1 （异步发送/接收票据的线程）（队列采用生产者消费者模式）
	sendQueue sendWorderThread 发送票据队列
	receQueue receWorkerThread 接收票据队列
	zab协议（决定当前节点是否为leader）
	leader选举（当前节点的票据与接收到的票据集合中的票据比较，若当前票据在集合中的数量过半则认为是leader，这也就是所谓的过半节点同意则选举为leader的说法）
	若

    server2


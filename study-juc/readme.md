## 问题1：AQS是什么？
    分析：
    解答：
    1、AQS是 AbstractQueuedSynchronizer 的简称，他是一个队列同步器，被设计为一个基础同步器的框架；
    2、支持 共享锁模式 和 独占锁模式
    3、AQS本质上是一个双向链表队列，其中通过一个共享的同步状态、当前持有锁的线程来标记锁的状态、另外还含有 队列头部节点、尾部节点的字段，用来实现双向链表

## 问题2：ReentrantLock中公平锁和非公平锁的区别？
    分析：可通过源码来分析两者的区别
    解答：
    公平锁是按照AQS队列的顺序排队获取锁，
    非公平锁是直接通过CAS修改同步状态尝试获取锁，成功就返回，不成功则再按照AQS队列的顺序排队获取锁，从而达到不排队就直接抢占锁的目的。

## volatile 的原理

## synchronized 的原理



## 基于TreeMap分析并理解红黑树结构

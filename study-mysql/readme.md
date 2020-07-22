1. InnoDB 存储引擎的内存结构
2.MySQL查询的执行流程
3.磁盘数据如何加载到InnoDB内存（脏页落盘机制）
4. RC 和 RR 隔离级别下，如何加锁及使用 MVCC 机制读取数据
5. undolog 和 ReadView 如何实现事务隔离性
6. redolog 如何实现事务的持久性和一致性


数据结构动图网址
> https://www.cs.usfca.edu/~galles/visualization/Algorithms.html
>

MYSQL核心
1、查询SQL执行流程
2、更新SQL执行流程
3、存储引擎
Innodb存储引擎逻辑结构
4、索引 - B+Tree
5、事务
6、锁MVCC
7、主从复制
8、Explain
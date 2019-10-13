# 集群
- 环境搭建的步骤 - 青山
> https://gper.club/personal/article/1888


## Sentinel
- sentinel 集群中使用Raft算法进行leader选举，选举出来以后，再通过发送slaveof on one给redis的slave，然后选出redis的master 
> http://thesecretlivesofdata.com/raft/
>
>

redis-cluster - 集群
     redis-7291
     redis-7292
     redis-7293

- Codis
- TwemProxy

分布式
数据均匀分布 是怎么实现的？
> 一致性hash算法，
> redis是采用 slot 虚拟槽来实现的数据均匀分布
> CRC16 算法 - 16384 个槽 - 找到key对应的slot
> Redis Group1 - 0,5460
> Redis Group2 - 5461,10922
> Redis Group3 - 10923,16383
> 通过命令 cluster keyslot keyname 找到 key 所在的slot
>
>


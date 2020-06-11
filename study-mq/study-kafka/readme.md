## 参考博客
阿里中间件团队博客
> http://jm.taobao.org/2016/04/07/kafka-vs-rocketmq-topic-amout/

CSDN 博客
> https://blog.csdn.net/damacheng/article/details/42846549


## 重置offset

Consumer的Offset信息不再默认保存在Zookeeper上，而是选择用Topic的形式保存下来。在命令行中可以使用kafka-consumer-groups的脚本实现Offset的相关操作。

> 1、consumer group状态必须是inactive的，即不能是处于正在工作中的状态。
>
> 2、启动consumer

Topic的作用域
```text
--all-topics：为consumer group下所有topic的所有分区调整位移）
--topic t1 --topic t2：为指定的若干个topic的所有分区调整位移
--topic t1:0,1,2：为指定的topic分区调整位移
```


重置策略
```text
--to-earliest：把位移调整到分区当前最小位移
--to-latest：把位移调整到分区当前最新位移
--to-current：把位移调整到分区当前位移
--to-offset <offset>： 把位移调整到指定位移处
--shift-by N： 把位移调整到当前位移 + N处，注意N可以是负数，表示向前移动
--to-datetime <datetime>：把位移调整到大于给定时间的最早位移处，datetime格式是yyyy-MM-ddTHH:mm:ss.xxx，比如2017-08-04T00:00:00.000
--by-duration <duration>：把位移调整到距离当前时间指定间隔的位移处，duration格式是PnDTnHnMnS，比如PT0H5M0S
--from-file <file>：从CSV文件中读取调整策略
```


执行方案
什么参数都不加：只是打印出位移调整方案，不具体执行
> --execute：执行真正的位移调整
>
> --export：把位移调整方案按照CSV格式打印，方便用户成csv文件，供后续直接使用


更新到当前group最初的offset位置
```shell script
bin\windows\kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group ConsumerSubscribeGroup --reset-offsets --all-topics --to-earliest --execute
```

更新到指定的offset位置
```shell script
bin\windows\kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group ConsumerSubscribeGroup --reset-offsets --all-topics --to-offset 5 --execute
```

更新到当前offset位置（解决offset的异常）
```shell script
bin\windows\kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group ConsumerSubscribeGroup --reset-offsets --all-topics --to-current --execute
```

offset位置按设置的值进行位移
```shell script
bin\windows\kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group ConsumerSubscribeGroup --reset-offsets --all-topics --shift-by -100000 --execute
```

offset设置到指定时刻开始
```shell script
bin\windows\kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group ConsumerSubscribeGroup --reset-offsets --all-topics --to-datetime 2017-08-04T14:30:00.000
```
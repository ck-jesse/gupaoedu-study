## Java CPU 100% 排查技巧
### 1、使用top命令查看cpu占用资源较高的PID

![1563435057073](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1563435057073.png)

    当前占用cup100% 的PID为3455。

### 2、通过`jps`找到当前用户下的java程序PID
    执行jps -l能够打印出所有的应用的PID，找到有一个PID和这个cpu使用100%一样的ID！！！就知道是哪一个服务了。知道了对应的服务，在接着后续的分析步骤。

### 3、使用 `pidstat -p < PID > 1 3 -u -t`  ，显示线程的统计信息
    -p：指定进程号
    -u：默认的参数，显示各个进程的cpu使用统计
    -t：显示选择任务的线程的统计信息外的额外信息

![1563435332226](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1563435332226.png)

![1563435339680](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1563435339680.png)



### 4、找到cpu占用较高的线程TID 

~~~
找到cpu占用较高的线程TID ，通过上图发现是 `3467`的TID占用cup较大
~~~



### 5、将TID转换为十六进制

    因为jstack命令输出文件记录的线程ID是16进制。因此我们先将TID转换为十六进制的表示方式。


​    
### 6、通过`jstack [-l] PID`输出当前进程的线程信息
~~~
jstack -l PID /temp/test.log
~~~

### 7、查找 TID对应的线程(输出的线程id为十六进制)，找到对应的代码，使用命令查找哦，不要肉眼比对，具体命令请思考，给你表现机会。

~~~
找到之后具体分析这个线程在干什么，为什么会占用这么多的 CUP资源。
~~~



![1563435536091](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1563435536091.png)



~~~
PS：线程的几种状态如下说明：

NEW,未启动的。不会出现在Dump中。
RUNNABLE,在虚拟机内执行的。
BLOCKED,受阻塞并等待监视器锁。
WATING,无限期等待另一个线程执行特定操作。
TIMED_WATING,有时限的等待另一个线程的特定操作。
TERMINATED,已退出的。

~~~



~~~
也可以通过使用jstack找到系统的代码性能问题

1、在进行压力测试的时候，使用jps找到应用的PID

2、然后使用jstack输出出压力测试时候应用的dump信息

3、分析输出的日志文件中那个方法block线程占用最多，这里可能是性能有问题，找到对应的代码分析

~~~





## 阿里开源的Java诊断工具arthas（问题定位神器）



~~~
官网地址：arthas[3] ：https://alibaba.github.io/arthas/index.html

Arthas 是Alibaba开源的Java诊断工具，深受开发者喜爱。

当你遇到以下类似问题而束手无策时，Arthas可以帮助你解决：

这个类从哪个 jar 包加载的？为什么会报各种类相关的 Exception？
我改的代码为什么没有执行到？难道是我没 commit？分支搞错了？
遇到问题无法在线上 debug，难道只能通过加日志再重新发布吗？
线上遇到某个用户的数据处理有问题，但线上同样无法 debug，线下无法重现！
是否有一个全局视角来查看系统的运行状况？
有什么办法可以监控到JVM的实时运行状态？

~~~


使用步骤
1、mvn package
2、拷贝dependencies jar包到apache-jmeter-5.2\lib\ext下
id-generator-jmeter-jar-with-dependencies.jar
3、启动jmeter后，新建：
选中测试计划->右键->添加->线程->线程组
选中线程组->右键->添加->取样器->Java请求
选择类名称。

![1576057527374](C:\Users\Admin\AppData\Roaming\Typora\typora-user-images\1576057527374.png)
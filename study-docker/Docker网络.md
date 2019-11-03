



网卡

network namespace  网络命名空间

不同的network namespace通过verth-pair技术建立同一网段的组件连接，所以能通信。

资源隔离，网络隔离

基于镜像创建一个docker container时，会创建一个自己的network namespace，所以多个container可以进行通信。

不同的container能ping通，是因为有一个公共的docker0的网络，通过veth-pair技术与容器建立network namespace配置，所以能ping通。

docker0 是通过 bridge 桥接网络，建立不同容器间的通信的。

centos7 安装docker后，通过 ip a 命令可查看到一个 docker0  的网卡。

~~~
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
    inet6 ::1/128 scope host 
       valid_lft forever preferred_lft forever
注意：lo 表示本地网络的网卡
2: eth0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP group default qlen 1000
    link/ether 52:54:00:8a:fe:e6 brd ff:ff:ff:ff:ff:ff
    inet 10.0.2.15/24 brd 10.0.2.255 scope global noprefixroute dynamic eth0
       valid_lft 83212sec preferred_lft 83212sec
    inet6 fe80::5054:ff:fe8a:fee6/64 scope link 
       valid_lft forever preferred_lft forever
注意：eth0 表示对外网的网卡
3: eth1: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP group default qlen 1000
    link/ether 08:00:27:85:88:c5 brd ff:ff:ff:ff:ff:ff
    inet 192.168.3.39/24 brd 192.168.3.255 scope global noprefixroute dynamic eth1
       valid_lft 83211sec preferred_lft 83211sec
    inet6 fe80::a00:27ff:fe85:88c5/64 scope link 
       valid_lft forever preferred_lft forever
注意：eth1 表示本地的物理网络网卡
4: docker0: <NO-CARRIER,BROADCAST,MULTICAST,UP> mtu 1500 qdisc noqueue state DOWN group default 
    link/ether 02:42:fb:75:c8:a9 brd ff:ff:ff:ff:ff:ff
    inet 172.17.0.1/16 brd 172.17.255.255 scope global docker0
       valid_lft forever preferred_lft forever
注意：docker0 表示安装docker后生成的网段

~~~



docker run -d --name tomcat01 -p 8081:8080 tomcat

docker run -d --name tomcat02 -p 8082:8080 tomcat

创建了两个tomcat容器，在centos7中通过 ip a ，发现多了下面的个网络网卡

~~~
6: veth9f2049c@if5: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue master docker0 state UP group default 
    link/ether 46:0c:5b:83:eb:9e brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet6 fe80::440c:5bff:fe83:eb9e/64 scope link 
       valid_lft forever preferred_lft forever
注意：veth9f2049c@if5 与 tomcat01 的 eth0@if6 配对
8: vethcb0d4ef@if7: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue master docker0 state UP group default 
    link/ether 4e:2c:0e:1e:3c:77 brd ff:ff:ff:ff:ff:ff link-netnsid 1
    inet6 fe80::4c2c:eff:fe1e:3c77/64 scope link 
       valid_lft forever preferred_lft forever
注意：vethcb0d4ef@if7 与 tomcat02 的 eth0@if8 配对
~~~

tomcat01 上执行 ip a后

docker exec -it tomcat01 ip a

~~~
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
5: eth0@if6: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:ac:11:00:02 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 172.17.0.2/16 brd 172.17.255.255 scope global eth0
       valid_lft forever preferred_lft forever

~~~



tomcat02 上执行 ip a后

docker exec -it tomcat02 ip a

~~~
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
7: eth0@if8: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:ac:11:00:03 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 172.17.0.3/16 brd 172.17.255.255 scope global eth0
       valid_lft forever preferred_lft forever

~~~





查看网络

docker network ls

~~~
NETWORK ID          NAME                DRIVER              SCOPE
7311e75b97eb        bridge              bridge              local
a9c895fba5c3        host                host                local
12d619786168        none                null                local
~~~

docker0 的默认网段是172.17.0.xxx

创建一个网段tomcat-net

docker network create tomcat-net



自定义bridge桥接网络

一个容器通过名字去访问另一个容器。

如springboot项目container去访问mysql container的，因为IP是不固定的，如果通过IP去访问时，那么将会很坑爹，所以想法是通过容器的名字去访问。

docker exec -it tomcat11 ping tomcat22

docker run -d --name mysql01 --network mysql-network mysql



docker 多机网络通信的问题

vxlan 用于解决多机网络通信的问题，底层是 overlay来实现的。



iptables



官网的资料是最权威的，学习的时候，可以先了解官网，然后再针对别的资料进行学习，回过头来再阅读官网。
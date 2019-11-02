# Docker 容器

VM 

需要guest os操作系统，那么创建VM的时候，就已经确定了要多少空间

Container

需要Docker支持，暂用资源少，动态分配



- Docker Engine

- Image - 镜像
  - 相当于Java中Class文件
  - 模板，基于镜像来部署容器

- Containers - 容器
  - 相当于Java中的对象
  - 基于镜像创建的容器



## 本地环境搭建Centos环境

>  在Win10上安装虚拟机，然后在虚拟机上安装Docker
>
> Win10OS --> VM --> VMOS --> Docker --> App

win10 -> IP=192.168.8.185:9090

Centos -> IP=192.168.8.103:8080

容器 -> linux ->container -> tomcat -> IP=xxx:8080





本地环境搭建Centos虚拟机环境，用于玩转Docker+K8s

工具 vagrant + virtualBox 来搭建Centos虚拟机

> Vagrant是一个基于Ruby的工具，用于创建和部署虚拟化开发环境。它 使用Oracle的开源[VirtualBox](https://baike.baidu.com/item/VirtualBox)虚拟化系统，使用 Chef创建自动化虚拟环境。
>
> 安装手册
>
> https://www.cnblogs.com/hafiz/p/9175484.html
>
> 





## 镜像

http://hub.docker.com

阿里云镜像加速器

https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors



设置docker阿里云的镜像仓库

> sudo yum-config-manager --add-repo  http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo





## springboot项目基于docker的部署

> 1、正常打包
>
> > 可通过 java -jar xxx.jar 来运行
>
> 2、编写Dockerfile文件 
>
> > 有自己的语法，实际就是一系列命令的组合
>
> 3、构建 Image 镜像
>
> > docker build   基于 Dockerfile 构建镜像；
> >
> > 注意：Image 构建好以后，就不能修改了。
>
> 4、推送到docker仓库
>
> > 注意：push 推送 image 到 仓库时，需要知道仓库服务器，账号/密码
> >
> > 官方仓库 - docker.hub 
> >
> > ​	登录： docker login
> >
> > ​	打标签： docker tag
> >
> > ​	推送： docker push
> >
> > 阿里云仓库 
> >
> > 
>
> 5、从docker仓库pull镜像
>
> docker pull image:version
>
> 6、基于镜像启动一个 container 容器
>
> docker run -d --name tomcat01 -p 9090:8080 tomcat
>
> 注意：
>
> 7、连接到 container 容器
>
> docker exec -it tomcat01 /bin/bash
>
> 



jack的阿里云镜像

> docker pull registry.cn-hangzhou.aliyuncs.com/itcrazy2016/springboot-demo:v1.0



docker 仓库私服

> 主流harbor
>
> http://github.com/goharbor/harbor
>
> 



docker compose 容器管理

> 



## 容器

容器监控 - weavescope

> github : waaveworks/scope



容器间的通信

> springboot 访问 redis/mq/mysql -> ip地址 -> iptables (网络)



容器的持久化

> 数据 -> 保存


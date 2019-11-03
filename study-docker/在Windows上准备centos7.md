# 在Windows上准备centos7

> 采用vagrant+virtualbox

## 下载安装vagrant

>01访问Vagrant官网 
>https://www.vagrantup.com/
>02点击Download 
>Windows，MacOS，Linux等 
>03选择对应的版本
>04傻瓜式安装 
>05命令行输入vagrant，测试是否安装成功

## 下载安装virtualbox

> 01访问VirtualBox官网
>
> https://www.virtualbox.org/
> 02选择左侧的“Downloads”
> 03选择对应的操作系统版本
> 04傻瓜式安装 
> 05[win10中若出现]安装virtualbox快完成时立即回滚，并提示安装出现严重错误
>
> (1)打开服务
>
> (2)找到DeviceInstallService和DeviceSetupManager，然后启动
>
> (3)再次尝试安装

## 安装centos7

- 01 准备centos7的box

  > 注意：因为 vagrant up 从远端拉去centos7系统太慢，所以先下载centos7到本地。

```
(1) 选中命令行中提示的链接

https://vagrantcloud.com/centos/boxes/7/versions/1905.1/providers/virtualbox.box

(2) 复制到迅雷中下载，下载目录： D:\vagrant

(3) vagrant box add centos/7 D:\vagrant\virtualbox.box

注意：该命令在本地只需要执行一次，centos7就已经被添加到本地box中，后续可用他来创建多个centos/7虚拟机。

(4) vagrant box list

查看本地的box[这时候可以看到centos/7]
```

- 02 创建 first-docker-centos7 文件夹，并进入其中

  > [目录路径不要有中文字符]

- 03 在此目录下打开cmd，初始化 centos/7

  > vagrant init centos/7
  >
  > 此时会在当前目录下生成Vagrantfile 

- 04 根据本地的centos7 box创建虚拟机

  > vagrant up 
  >
  > [打开virtualbox，可以发现centos7创建成功]
  >
  > 注意：vagrant up会找centos7的镜像，本地有就用本地的，本地没有就会拉取远端的。

> 注意：可重复上面的 02 到 04 步骤，来在本地创建多个centos虚拟机。但是要创建不同的文件夹。

- 05 vagrant 基本操作

```
(1)vagrant ssh
进入刚才创建的centos7中

(2)vagrant status
查看centos7的状态

(3)vagrant halt
停止centos7

(4)vagrant destroy
删除centos7 

(5)vagrant status
查看当前vagrant创建的虚拟机

(6)重启 - vagrant reload

(7)启动 - vagrant up

(8)暂停 - vagrant suspend

(9)恢复 - vagrant resume

(7)Vagrantfile中也可以写脚本命令，使得centos7更加丰富
但是要注意，修改了Vagrantfile，要想使正常运行的centos7生效，必须使用vagrant reload

```

至此，使用vagrant+virtualbox搭建centos7完成，后面可以修改Vagrantfile对虚拟机进行相应配置。



## 通过Xshell连接

- 01 查看centos7相关信息

  > vagrant ssh-config
  >
  > 关注:Hostname Port  IdentityFile

- 02 Xshell连接

  > IP:127.0.0.1 
  > port:2222 
  > 用户名:vagrant 
  > 密码:vagrant 
  > 文件:Identityfile指向的路径

- 03 使用root账户登录

  > sudo -i 
  > vi /etc/ssh/sshd_config 
  > 修改 PasswordAuthentication yes
  >
  > 修改密码
  >
  > passwd root 输入root
  >
  > systemctl restart sshd 
  >
  > root vagrant 登录



## box的打包分发

- 01 退出虚拟机

  > vagrant halt

- 02 打包

  > vagrant package --output first-docker-centos7.box

- 03 得到first-docker-centos7.box

- 04 将first-docker-centos7.box添加到其他的vagrant环境中

  > vagrant box add first-docker-centos7 first-docker-centos7.box

- 05 得到Vagrantfile

  > vagrant init first-docker-centos7

- 06 根据Vagrantfile启动虚拟机

  > agrant up[此时可以得到和之前一模一样的环境，但是网络要重新配置]



## 安装docker

- 1 进入centos7

  > vagrant ssh

- 2 配置阿里镜像加速器

  >阿里云镜像加速器官网： https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors
  >
  >通过修改daemon配置文件/etc/docker/daemon.json来使用加速器
  >
  >sudo mkdir -p /etc/docker
  >sudo tee /etc/docker/daemon.json <<-'EOF'
  >{
  >"registry-mirrors": ["https://dqabg8lv.mirror.aliyuncs.com"]
  >}
  >EOF
  >sudo systemctl daemon-reload

- 3 卸载之前的docker

  > sudo yum remove docker \ docker-client \ docker-client-latest \ docker-common \ docker-latest \ docker-latest-logrotate \ docker-logrotate \ docker-engine 
  
- 4 安装必要的依赖

  > sudo yum install -y yum-utils \ device-mapper-persistent-data \ lvm2
  >
  
- 5 设置docker仓库

> 阿里云镜像仓库【推荐，速度快】
>
> sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

> 官方仓库
>
> sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

- 6 安装docker

  > sudo yum install -y docker-ce docker-ce-cli containerd.io

- 7 启动docker

  > sudo systemctl start docker && sudo systemctl enable docker

- 8 测试docker安装是否成功

  > sudo docker run hello-world



## docker基本体验

- 01创建tomcat容器

> docker pull tomcat 
> docker run -d --name my-tomcat -p 9090:8080 tomcat

- 02创建mysql容器

> docker run -d --name my-mysql -p 3301:3306 -e MYSQL_ROOT_PASSWORD=jack123 --privileged mysql

- 03进入到容器里面

> docker exec -it containerid /bin/bash


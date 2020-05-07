# 实用 `Linux`命令

## 查看CPU过高的进程
```shell script
ps -aux | sort -rnk 3 | head -20
```
## 查看内存过高的进程
```shell script
ps -aux | sort -rnk 4 | head -20
```
## 查看当前路径下一级目录的空间占用信息
```shell script
du -h -d1
```
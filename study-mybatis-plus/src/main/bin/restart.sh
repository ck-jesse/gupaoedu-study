#!/bin/bash

#======================================================================
# 项目重启shell脚本
# 先调用shutdown.sh停服
# 然后调用startup.sh启动服务
#
# author: chenck
# date: 2018-12-28
#======================================================================

# 项目名称
APPLICATION="study-mybatis-plus"
echo ""
# 停服
echo "======> stop ${APPLICATION}"
sh shutdown.sh ${APPLICATION}

# 启动服务
echo "======> start ${APPLICATION}"
sh start.sh ${APPLICATION}
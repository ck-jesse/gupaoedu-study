package com.coy.gupaoedu.study.mybatisplus.service;

import com.coy.gupaoedu.study.mybatisplus.dao.service.Table1Service;
import com.coy.gupaoedu.study.mybatisplus.dto.Table1Data;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chenck
 * @date 2023/2/7 13:12
 */
@Log
@Service
public class Table1ServiceImpl {

    @Autowired
    private Table1Service table1Service;

    public void findRootNode() {

        // 查询所有的数据
        /*List<Table1Entity> list = table1Service.list();

        for (Table1Entity table1Entity : list) {

        }*/

        // 递归查询最顶层的上级
        Table1Data table1Data = table1Service.getBaseMapper().findRootNode("9A06B5F73FBC178203183EF7C564C0F5");
        System.out.println("根节点 " + table1Data);

        // 更新root标识
        int rslt = table1Service.getBaseMapper().updateRootFlag(table1Data.getId());
        System.out.println("根节点标记更新 " + rslt);

        // 查询叶子节点
        List<String> list = table1Service.getBaseMapper().findChildNode("A4B7B61D21544E943FFA548011DBF270");
        System.out.println("叶子节点 " + list);


    }

}

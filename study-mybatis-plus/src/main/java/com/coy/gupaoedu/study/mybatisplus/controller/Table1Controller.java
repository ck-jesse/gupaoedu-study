package com.coy.gupaoedu.study.mybatisplus.controller;

import com.coy.gupaoedu.study.mybatisplus.common.domain.DataResponse;
import com.coy.gupaoedu.study.mybatisplus.service.Table1ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenck
 * @date 2023/2/7 13:14
 */
@RestController
public class Table1Controller {

    @Autowired
    Table1ServiceImpl table1ServiceImpl;

    /**
     * 查询店铺图片
     * <p>
     * http://localhost:8080/table1/find_root
     */
    @GetMapping(value = "/table1/find_root")
    public DataResponse findRoot() {
        table1ServiceImpl.findRootNode();
        return new DataResponse();
    }
}

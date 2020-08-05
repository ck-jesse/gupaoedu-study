package com.coy.gupaoedu.study.mybatisplus.controller;

import com.coy.gupaoedu.study.mybatisplus.dto.ShopImageVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenck
 * @date 2020/8/5 13:50
 */
@RestController
@RequestMapping("oom")
public class OOMController {

    List<ShopImageVO> list = new ArrayList<>();

    @RequestMapping("heap")
    public void heapOom() {
        System.out.println("验证堆溢出");
        while (true) {
            list.add(new ShopImageVO("heapabcedfabcedfabcedfabcedfabcedfabcedfabcedfabcedfabcedfabcedfabcedfabcedfabcedfabcedfabcedfabcedfabcedfabcedf"));
        }
    }
}

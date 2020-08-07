package com.coy.gupaoedu.study.jvm.problem;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenck
 * @date 2020/8/7 14:22
 */
@RestController
public class CpuController {

    /**
     * * 模拟CPU占满
     */
    @GetMapping("/cpu/loop")
    public void testCPULoop() throws InterruptedException {
        System.out.println("请求cpu死循环");
        Thread.currentThread().setName("loop-thread-cpu");
        int num = 0;
        while (true) {
            num++;
            if (num == Integer.MAX_VALUE) {
                System.out.println("reset");
            }
            num = 0;
        }
    }
}

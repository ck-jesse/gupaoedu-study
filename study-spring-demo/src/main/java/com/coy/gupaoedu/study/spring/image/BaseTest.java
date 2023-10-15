package com.coy.gupaoedu.study.spring.image;

import org.junit.After;
import org.junit.Before;
import org.springframework.util.StopWatch;

/**
 * @author chenck
 * @date 2023/10/15 17:19
 */
public class BaseTest {

    // 秒表
    StopWatch stopWatch = new StopWatch();

    @Before
    public void before() {
        stopWatch.start();
    }

    @After
    public void after() {
        stopWatch.stop();
        System.out.println("执行时间=" + stopWatch.getTotalTimeSeconds() + "s");
    }
}

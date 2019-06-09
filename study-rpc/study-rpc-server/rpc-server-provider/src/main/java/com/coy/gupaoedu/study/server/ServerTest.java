package com.coy.gupaoedu.study.server;

import com.coy.gupaoedu.study.server.rpc.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author chenck
 * @date 2019/6/9 16:00
 */
public class ServerTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        applicationContext.start();
    }
}

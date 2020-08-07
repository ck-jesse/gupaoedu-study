package com.coy.gupaoedu.study.jvm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Bootstrap {

    /**
     * 开启GC日志
     * <p>
     * -Xmx128M -Xms128M -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:E:\temp\gclog\gc_%t.log  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=heap_%t.hprof
     */
    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

}
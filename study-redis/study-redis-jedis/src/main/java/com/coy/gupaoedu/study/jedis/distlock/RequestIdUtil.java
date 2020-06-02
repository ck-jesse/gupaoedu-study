package com.coy.gupaoedu.study.jedis.distlock;

import java.util.UUID;

/**
 * @author chenck
 * @date 2020/6/1 20:48
 */
public class RequestIdUtil {

    public static String requestId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}

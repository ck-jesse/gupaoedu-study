package com.coy.gupaoedu.study.spring.common;

import org.slf4j.MDC;

import java.util.Map;

/**
 * Runnable 包装 MDC
 *
 * @author chenck
 * @date 2020/9/23 19:37
 */
public class RunnableWarpper implements Runnable {

    Runnable runnable;
    Map<String, String> contextMap;

    public RunnableWarpper(Runnable runnable) {
        this.runnable = runnable;
        this.contextMap = MDC.getCopyOfContextMap();
    }

    @Override
    public void run() {
        try {
            if (null != contextMap) {
                MDC.setContextMap(contextMap);
            }
            runnable.run();
        } finally {
            if (null != contextMap) {
                MDC.clear();
            }
        }
    }
}

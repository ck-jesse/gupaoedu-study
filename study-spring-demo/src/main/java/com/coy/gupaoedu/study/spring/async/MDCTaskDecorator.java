package com.coy.gupaoedu.study.spring.async;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * MDC 内容传递
 * 注：MDC是基于ThreadLocal来实现的。
 *
 * @author chenck
 * @date 2020/3/13 21:44
 */
public class MDCTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        // 对Runnable进行装饰，将主线程的MDC内容设置到子线程的MDC中
        return new Runnable() {
            @Override
            public void run() {
                try {
                    MDC.setContextMap(contextMap);
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            }
        };
        /*return () -> {
            try {
                MDC.setContextMap(contextMap);
                runnable.run();
            } finally {
                MDC.clear();
            }
        };*/
    }
}

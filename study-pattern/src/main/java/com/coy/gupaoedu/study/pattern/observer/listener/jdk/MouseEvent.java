package com.coy.gupaoedu.study.pattern.observer.listener.jdk;

import java.util.EventObject;

/**
 * 鼠标事件
 *
 * @author chenck
 * @date 2019/3/19 16:14
 */
public class MouseEvent extends EventObject {
    /**
     * Constructs a prototypical Event. 限制来源为Mouse
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MouseEvent(Mouse source) {
        super(source);
    }
}

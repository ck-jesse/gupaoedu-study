package com.coy.gupaoedu.study.pattern.observer.listener.jdk;

/**
 * 鼠标移动事件
 *
 * @author chenck
 * @date 2019/3/19 16:16
 */
public class MouseMoveEvent extends MouseEvent {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MouseMoveEvent(Mouse source) {
        super(source);
    }

    public void move() {
        System.out.println("鼠标move事件被执行");
    }
}

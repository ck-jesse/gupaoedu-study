package com.coy.gupaoedu.study.pattern.observer.listener.jdk;

/**
 * 鼠标点击事件
 *
 * @author chenck
 * @date 2019/3/19 16:16
 */
public class MouseClickEvent extends MouseEvent {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MouseClickEvent(Mouse source) {
        super(source);
    }

    public void click() {
        System.out.println("鼠标click事件被执行");
    }
}

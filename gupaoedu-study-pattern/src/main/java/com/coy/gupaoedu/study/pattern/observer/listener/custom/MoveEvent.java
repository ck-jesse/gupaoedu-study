package com.coy.gupaoedu.study.pattern.observer.listener.custom;

/**
 * 移动事件
 *
 * @author chenck
 * @date 2019/3/19 15:56
 */
public class MoveEvent extends Event {

    public MoveEvent(Object source) {
        super(source);
    }

    public void move() {
        System.out.println("move事件被执行");
    }
}

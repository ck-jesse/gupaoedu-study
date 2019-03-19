package com.coy.gupaoedu.study.pattern.observer.listener.custom;

/**
 * 点击事件
 *
 * @author chenck
 * @date 2019/3/19 15:55
 */
public class ClickEvent extends Event {

    public ClickEvent(Object source) {
        super(source);
    }

    public void click() {
        System.out.println("click事件被执行");
    }

}

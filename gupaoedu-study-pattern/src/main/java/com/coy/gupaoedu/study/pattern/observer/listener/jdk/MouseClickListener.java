package com.coy.gupaoedu.study.pattern.observer.listener.jdk;

/**
 * 鼠标点击监听器
 *
 * @author chenck
 * @date 2019/3/19 16:18
 */
public class MouseClickListener implements MouseListener<MouseClickEvent> {
    @Override
    public void doEvent(MouseClickEvent event) {
        event.click();
        System.out.println("鼠标click事件的监听器被触发执行");
    }
}

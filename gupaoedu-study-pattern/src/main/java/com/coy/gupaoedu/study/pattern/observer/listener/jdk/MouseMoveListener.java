package com.coy.gupaoedu.study.pattern.observer.listener.jdk;

/**
 * 鼠标移动监听器
 *
 * @author chenck
 * @date 2019/3/19 16:19
 */
public class MouseMoveListener implements MouseListener<MouseMoveEvent> {
    @Override
    public void doEvent(MouseMoveEvent event) {
        event.move();
        System.out.println("鼠标move事件的监听器被触发执行");
    }
}

package com.coy.gupaoedu.study.pattern.observer.listener.custom;

/**
 * 移动事件的监听器
 *
 * @author chenck
 * @date 2019/3/19 14:18
 */
public class MoveListener implements Listener<MoveEvent> {

    @Override
    public void doEvent(MoveEvent event) {
        event.move();
        System.out.println("move事件的监听器被触发执行");
    }
}

package com.coy.gupaoedu.study.pattern.observer.listener.custom;

/**
 * 点击事件的监听器
 *
 * @author chenck
 * @date 2019/3/19 14:18
 */
public class ClickListener implements Listener<ClickEvent> {

    @Override
    public void doEvent(ClickEvent event) {
        event.click();
        System.out.println("click事件的监听器被触发执行");
    }
}

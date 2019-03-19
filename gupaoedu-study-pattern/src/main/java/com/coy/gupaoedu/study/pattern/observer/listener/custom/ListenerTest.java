package com.coy.gupaoedu.study.pattern.observer.listener.custom;

/**
 * 自定义实现：事件-监听机制
 * 包含三个角色：事件-事件源-监听器
 * 观察者模式的一种
 *
 * @author chenck
 * @date 2019/3/19 14:19
 */
public class ListenerTest {

    public static void main(String[] args) {
        EventSource eventSource = new EventSource();
        // 注册监听器
        eventSource.addListener("click", new ClickListener());
        eventSource.addListener("move", new MoveListener());

        eventSource.click("点击了");
        eventSource.move("移动了");
    }
}

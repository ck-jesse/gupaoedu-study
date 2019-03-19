package com.coy.gupaoedu.study.pattern.observer.listener.jdk;

/**
 * 基于JDK实现：事件-监听机制
 * 包含三个角色：事件-事件源-监听器
 * 观察者模式的一种
 *
 * @author chenck
 * @date 2019/3/19 14:19
 */
public class MouseListenerTest {

    public static void main(String[] args) {
        Mouse mouse = new Mouse();
        // 注册监听器
        mouse.addListener("click", new MouseClickListener());
        mouse.addListener("move", new MouseMoveListener());

        mouse.click("点击了鼠标");
        mouse.move("移动了鼠标");
    }
}

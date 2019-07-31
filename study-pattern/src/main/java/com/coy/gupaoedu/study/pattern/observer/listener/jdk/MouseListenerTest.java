package com.coy.gupaoedu.study.pattern.observer.listener.jdk;

/**
 * 基于JDK实现：事件-监听机制
 * 包含三个角色：事件-事件源-监听器
 * 观察者模式的一种，源-收听者(Listener)模式
 *
 * 注：spring中的事件机制就是基于JDK的事件-监听机制来实现的
 * 如：ApplicationListener 实现EventListener 和 ApplicationEvent 继承EventObject
 * 1、spring加载时会获取到所有实现了ApplicationListener的监听器，并维护到集合Set<ApplicationListener>中
 * 2、事件触发点的逻辑中，通过ApplicationEventPublisher#publishEvent(ApplicationEvent)来发布事件
 * 3、publishEvent中会根据事件来源source和事件类型type，从集合中匹配对应的ApplicationListener集合
 * 4、然后循环匹配的监听器集合，调用具体的事件处理方法ApplicationListener.onApplicationEvent(ContextRefreshedEvent等Event)
 *
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

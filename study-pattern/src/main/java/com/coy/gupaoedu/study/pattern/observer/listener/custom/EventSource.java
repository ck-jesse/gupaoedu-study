package com.coy.gupaoedu.study.pattern.observer.listener.custom;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件源：事件发生的地方
 * 角色：被观察者
 *
 * @author chenck
 * @date 2019/3/19 13:50
 */
public class EventSource {

    private Map<String, Listener> listenerMap = new HashMap();

    public void addListener(String eventName, Listener listener) {
        listenerMap.put(eventName, listener);
    }

    public void click(String msg) {
        System.out.println("click操作：" + msg);
        listenerMap.get("click").doEvent(new ClickEvent(this));
    }

    public void move(String msg) {
        System.out.println("move操作：" + msg);
        listenerMap.get("move").doEvent(new MoveEvent(this));
    }
}

package com.coy.gupaoedu.study.pattern.observer.listener.jdk;

import java.util.HashMap;
import java.util.Map;

/**
 * 鼠标：事件来源，即被观察者
 *
 * @author chenck
 * @date 2019/3/19 16:19
 */
public class Mouse {

    private Map<String, MouseListener> listenerMap = new HashMap();

    public void addListener(String eventName, MouseListener listener) {
        listenerMap.put(eventName, listener);
    }

    public void click(String msg) {
        System.out.println("鼠标click操作：" + msg);
        listenerMap.get("click").doEvent(new MouseClickEvent(this));
    }

    public void move(String msg) {
        System.out.println("鼠标move操作：" + msg);
        listenerMap.get("move").doEvent(new MouseMoveEvent(this));
    }
}

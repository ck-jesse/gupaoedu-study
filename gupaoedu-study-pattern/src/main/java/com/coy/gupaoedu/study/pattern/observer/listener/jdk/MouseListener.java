package com.coy.gupaoedu.study.pattern.observer.listener.jdk;

import java.util.EventListener;

/**
 * 鼠标监听器
 *
 * @author chenck
 * @date 2019/3/19 16:14
 */
public interface MouseListener<T extends MouseEvent> extends EventListener {

    public void doEvent(T event);
}

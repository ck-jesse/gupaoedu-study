package com.coy.gupaoedu.study.pattern.observer.listener.custom;

/**
 * 监听器
 * 角色：观察者
 *
 * @author chenck
 * @date 2019/3/19 13:45
 */
public interface Listener<T extends Event> {

    public void doEvent(T event);

}

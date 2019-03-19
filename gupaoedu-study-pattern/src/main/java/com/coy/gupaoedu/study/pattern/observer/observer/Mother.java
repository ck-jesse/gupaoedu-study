package com.coy.gupaoedu.study.pattern.observer.observer;

import java.util.Observable;

/**
 * 母亲作为一个 被观察者
 *
 * @author chenck
 * @date 2019/3/19 17:00
 */
public class Mother extends Observable {

    /**
     * 做饭
     *
     * @param
     * @author chenck
     * @date 2019/3/19 17:10
     */
    public void cook(String msg) {
        System.out.println("母亲：" + msg);
        // 改变通知者的状态
        setChanged();
        // 调用父类Observable方法，通知所有观察者
        notifyObservers();
    }
}

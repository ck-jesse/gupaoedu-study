package com.coy.gupaoedu.study.pattern.observer.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * 儿子作为一个观察者
 *
 * @author chenck
 * @date 2019/3/19 16:59
 */
public class Son implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("儿子：知道了，马上就来 " + arg);
    }
}

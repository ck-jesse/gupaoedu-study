package com.coy.gupaoedu.study.pattern.observer.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * 女儿作为一个观察者
 *
 * @author chenck
 * @date 2019/3/19 16:59
 */
public class Daughter implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("女儿：知道了，马上就来 " + arg);
    }
}

package com.coy.gupaoedu.study.pattern.observer.observer;

/**
 * 基于JDK的来实现观察者模式
 * <p>
 * 观察者模式（Observer）完美的将观察者和被观察的对象分离开。
 * 观察者模式有很多实现方式，从根本上说，该模式必须包含两个角色：观察者和被观察对象。
 * 一个目标物件（被观察者）管理所有相依于它的观察者物件，并且在它本身的状态改变时主动发出通知。
 *
 * @author chenck
 * @date 2019/3/19 17:06
 */
public class Main {

    public static void main(String[] args) {
        Mother mother = new Mother();
        mother.addObserver(new Son());
        mother.addObserver(new Daughter());
        mother.cook("饭做好了");
    }
}

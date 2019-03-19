package com.coy.gupaoedu.study.pattern.observer.observer;

/**
 * 基于JDK的来实现观察者模式
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

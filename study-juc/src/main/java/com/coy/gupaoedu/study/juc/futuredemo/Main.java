package com.coy.gupaoedu.study.juc.futuredemo;

/**
 * 启动类
 * 这是一个最简单的Future模式的实现，虽然简单，但是已经包含了Future模式中最精髓的部分。对大家理解JDK内部的Future对象，有着非常重要的作用。
 *
 * @author chenck
 * @date 2023/6/15 16:54
 */
public class Main {

    public static void main(String[] args) {
        Client client = new Client();
        //这里会立即返回，因为得到的是FutureData而不是RealData
        Data data = client.request("name");
        System.out.println("请求完毕");
        try {
            //这里可以用一个sleep代替了对其他业务逻辑的处理
            //在处理这些业务逻辑的过程中，RealData被创建，从而充分利用了等待时间
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        //使用真实的数据，如果到这里数据还没有准备好，getResult()会等待数据准备完，再返回
        System.out.println("数据 = " + data.getResult());
    }

}

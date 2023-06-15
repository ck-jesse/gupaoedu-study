package com.coy.gupaoedu.study.juc.futuredemo;

/**
 * 返回Data对象，立即返回FutureData，并开启ClientThread线程装配RealData
 *
 * @author chenck
 * @date 2023/6/15 16:54
 */
public class Client {

    //这是一个异步方法，返回的Data接口是一个Future
    public Data request(final String queryStr) {
        final FutureData future = new FutureData();
        new Thread() {
            public void run() {
                // RealData的构建很慢，所以在单独的线程中进行
                RealData realdata = new RealData(queryStr);
                //setRealData()的时候会notify()等待在这个future上的对象
                future.setRealData(realdata);
            }
        }.start();
        // FutureData会被立即返回，不会等待RealData被构造完
        return future;
    }

}

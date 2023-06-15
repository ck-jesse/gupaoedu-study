package com.coy.gupaoedu.study.juc.futuredemo;

/**
 * 相对的FutureData作为RealData的代理，类似于一个订单/契约，通过FutureData，可以在将来获得RealData。
 * <p>
 * 理解：等同Java中的Future，Future模式中的Future，类似一个合约，一份承诺。
 * 虽然订单不能吃，但是手握订单，不怕没吃的，虽然Future不是我们想要的结果，但是拿着Future就能在将来得到我们想要的结果。
 *
 * @author chenck
 * @date 2023/6/15 16:52
 */
public class FutureData implements Data {
    // 内部需要维护RealData
    protected RealData realdata = null;
    protected boolean isReady = false;

    public synchronized void setRealData(RealData realdata) {
        if (isReady) {
            return;
        }
        this.realdata = realdata;
        isReady = true;
        //RealData已经被注入，通知getResult()
        notifyAll();
    }

    //会等待RealData构造完成
    public synchronized String getResult() {
        while (!isReady) {
            try {
                //一直等待，直到RealData被注入
                wait();
            } catch (InterruptedException e) {
            }
        }
        //真正需要的数据从RealData获取
        return realdata.result;
    }

}

package com.coy.gupaoedu.study.pattern.chainofresponsibility;

/**
 * 抽象处理者（审批者类）
 *
 * @author chenck
 * @date 2019/3/21 20:38
 */
public abstract class Approver {
    /**
     * 定义后继处理对象
     */
    protected Approver successor;

    //设置后继者
    public void setSuccessor(Approver successor) {
        this.successor = successor;
    }

    /**
     * 抽象请求处理方法
     *
     * @param
     * @author chenck
     * @date 2019/3/21 20:39
     */
    public abstract void processRequest(PurchaseRequest request);
}

package com.coy.gupaoedu.study.pattern.chainofresponsibility;

/**
 * 请求消息类（采购单）
 *
 * @author chenck
 * @date 2019/3/21 20:38
 */
public class PurchaseRequest {

    private double amount;  //采购金额
    private int number;  //采购单编号
    private String purpose;  //采购目的

    // 构造器
    public PurchaseRequest(double amount, int number, String purpose) {
        this.amount = amount;
        this.number = number;
        this.purpose = purpose;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}

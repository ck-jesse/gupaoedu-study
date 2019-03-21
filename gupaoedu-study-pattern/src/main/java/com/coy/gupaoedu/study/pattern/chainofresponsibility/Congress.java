package com.coy.gupaoedu.study.pattern.chainofresponsibility;

/**
 * 董事会类：具体处理者
 *
 * @author chenck
 * @date 2019/3/21 20:42
 */
public class Congress extends Approver {
    @Override
    public void processRequest(PurchaseRequest request) {
        //处理请求
        System.out.println("召开董事会审批采购单：" + request.getNumber() + "，金额：" + request.getAmount() + "元，采购目的：" + request.getPurpose() + "。");
    }
}

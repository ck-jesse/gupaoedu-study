package com.coy.gupaoedu.study.pattern.chainofresponsibility;

/**
 * 董事长类：具体处理者
 *
 * @author chenck
 * @date 2019/3/21 20:41
 */
public class President extends Approver {

    @Override
    public void processRequest(PurchaseRequest request) {
        if (request.getAmount() < 500000) {
            // 处理请求
            System.out.println("董事长" + "审批采购单：" + request.getNumber() + "，金额：" + request.getAmount() + "元，采购目的：" + request.getPurpose() + "。");
        } else {
            // 转发请求
            this.successor.processRequest(request);
        }
    }
}

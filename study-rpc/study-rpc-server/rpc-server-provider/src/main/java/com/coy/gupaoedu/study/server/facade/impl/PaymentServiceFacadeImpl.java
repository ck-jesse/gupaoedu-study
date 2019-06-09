package com.coy.gupaoedu.study.server.facade.impl;

import com.coy.gupaoedu.study.server.facade.PaymentServiceFacade;
import com.coy.gupaoedu.study.server.rpc.RpcService;

/**
 * @author chenck
 * @date 2019/6/9 16:41
 */
@RpcService(value = PaymentServiceFacade.class, version = "v1.0")
public class PaymentServiceFacadeImpl implements PaymentServiceFacade {
    @Override
    public String pay(String payInfo) {
        System.out.println("[server][pay]receive request: " + payInfo);
        return "pay_success";
    }
}

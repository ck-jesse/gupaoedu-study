package com.coy.gupaoedu.study.server.facade.impl;

import com.coy.gupaoedu.study.server.dto.User;
import com.coy.gupaoedu.study.server.facade.HelloServiceFacade;
import com.coy.gupaoedu.study.server.rpc.RpcService;

/**
 * @author chenck
 * @date 2019/6/6 16:44
 */
@RpcService(value = HelloServiceFacade.class)
public class HelloServiceFacadeImpl implements HelloServiceFacade {

    @Override
    public String sayHello(String content) {
        System.out.println("[server][sayHello] receive request: " + content);
        return "Hello，I'm the server.";
    }

    @Override
    public String saveUser(User user) {
        System.out.println("[server][saveUser] receive request: " + user);
        return "Save user success";
    }
}

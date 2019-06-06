package com.coy.gupaoedu.study.server.facade;

import com.coy.gupaoedu.study.server.dto.User;

/**
 * @author chenck
 * @date 2019/6/6 16:33
 */
public interface HelloServiceFacade {

    public String sayHello(String content);
    public String saveUser(User user);
}

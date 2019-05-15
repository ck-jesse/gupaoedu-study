package com.coy.gupaoedu.study.pattern.proxy;

public class UserServiceImpl implements UserService {

    @Override
    public String printUserInfo(String name) {
        System.out.println(name);
        return "success";
    }

}

package com.coy.gupaoedu.study.server.dto;

import java.io.Serializable;

/**
 * @author chenck
 * @date 2019/6/6 16:47
 */
public class User implements Serializable {

    private String name;
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

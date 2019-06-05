package com.coy.gupaoedu.study.spring.demo.model;

import java.io.Serializable;

/**
 * @author chenck
 * @date 2019/6/3 11:46
 */
public class User implements Serializable {

    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

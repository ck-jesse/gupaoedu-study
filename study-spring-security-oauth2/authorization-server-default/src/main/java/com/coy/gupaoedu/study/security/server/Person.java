package com.coy.gupaoedu.study.security.server;

import lombok.Data;

/**
 * @author chenck
 * @date 2019/9/29 23:03
 */
@Data
public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

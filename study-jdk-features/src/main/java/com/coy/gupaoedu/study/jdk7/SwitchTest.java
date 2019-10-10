package com.coy.gupaoedu.study.jdk7;

/**
 * java7 中 switch可以使用字符串
 */
public class SwitchTest {

    public static void main(String[] args) {
        String type = "wechat";
        switch (type) {
            case "wechat":
                System.out.println("wechat");
                break;
            case "alipay":
                System.out.println("alipay");
                break;
            default:
                System.out.println("default");
        }
    }
}

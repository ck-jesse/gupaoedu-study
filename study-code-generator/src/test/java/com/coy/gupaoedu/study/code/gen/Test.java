package com.coy.gupaoedu.study.code.gen;

import com.coy.gupaoedu.study.code.gen.util.StringUtils;

import java.util.Arrays;

/**
 * @author chenck
 * @date 2020/8/21 18:27
 */
public class Test {

    public static void main(String[] args) {
        String[] str = StringUtils.split(StringUtils.substringBetween("int(11)", "(", ")"), ",");
        Arrays.stream(str).forEach(s -> System.out.println(s));
    }
}

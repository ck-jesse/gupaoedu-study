package com.coy.gupaoedu.study.guava;

import com.google.common.base.CaseFormat;

/**
 * @author chenck
 * @date 2019/10/18 16:02
 */
public class CaseFormatTest {

    public static void main(String[] args) {

        System.out.println(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, "test-data"));// 横线 转 驼峰
        System.out.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "test_data"));// 下划线 转 驼峰
        System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "test_data"));// 下划线 转 驼峰

        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "testdata"));// 驼峰 转 下划线（因为不是驼峰，所以不转）
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "TestData"));// 驼峰 转 下划线
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "testData"));// 驼峰 转 横线
    }
}

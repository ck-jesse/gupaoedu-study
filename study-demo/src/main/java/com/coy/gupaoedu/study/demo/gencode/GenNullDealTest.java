package com.coy.gupaoedu.study.demo.gencode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成初始化代码，用于将普通DTO copy 到proto实体时避免null等异常
 *
 * @author chenck
 * @date 2020/2/28 10:15
 */
public class GenNullDealTest {

    public static void main(String[] args) throws FileNotFoundException {

        String filePath = "E:\\temp\\NullDeal.txt";
        List<String> list = readFile(filePath);

        for (String line : list) {
            if (!line.startsWith("private") && !line.startsWith("public")) {
                continue;
            }
            String[] arr = line.replaceAll(";", "").split(" ");
            String fieldType = arr[1];
            String fieldName = arr[2];

            if ("String".equals(fieldType)) {
                buildNulDeal(fieldName, "\"\"");
            } else if ("Long".equals(fieldType)) {
                buildNulDeal(fieldName, "0L");
            } else if ("Integer".equals(fieldType)) {
                buildNulDeal(fieldName, "0");
            } else if ("Double".equals(fieldType)) {
                buildNulDeal(fieldName, "0");
            } else if ("BigDecimal".equals(fieldType)) {
                buildNulDeal(fieldName, "BigDecimal.ZERO");
            }
        }
    }

    public static String buildNulDeal(String fieldName, String defaultValue) {
        StringBuilder sb = new StringBuilder();
        sb.append("if (null == ").append(fieldName).append(") {");
        sb.append(fieldName).append("=").append(defaultValue).append(";");
        sb.append("}");
        System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * 读取文件，文件内容格式如下：
     *
     * <p>
     *
     * </p>
     */
    public static List<String> readFile(String filePath) throws FileNotFoundException {
        List<String> list = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        try {
            FileReader fr = new FileReader(file);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                if (null == str || str.trim().length() == 0) {
                    continue;
                }
                str = str.trim();
                if (str.startsWith("syntax")
                        || str.startsWith("package")
                        || str.startsWith("option")
                ) {
                    System.out.println("过滤：" + str);
                    continue;
                }
                // 将多个空格替换为1个空格
                str = str.replaceAll("\\s+", " ");
                list.add(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
        return list;
    }
}

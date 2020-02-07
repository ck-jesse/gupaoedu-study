package com.coy.gupaoedu.study.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenck
 * @date 2020/2/6 17:09
 */
public class GeneratorProtoTest {

    public static final Map<String, String> typeMap = new HashMap<>();

    static {
        typeMap.put("String", "string");
        typeMap.put("Long", "uint64");
        typeMap.put("Integer", "uint32");
        typeMap.put("BigDecimal", "double");
    }

    public static void main(String[] args) throws FileNotFoundException {

        String filePath = "E:\\temp\\ModelToProto.txt";
        List<String> list = readFile(filePath);

        int rowIndex = 1;
        // 以2位步长
        for (int i = 0; i < list.size(); i = i + 2) {

            String descLine = list.get(i);
            String fieldLine = list.get(i + 1);
            buildProtoLine(descLine, fieldLine, rowIndex);
            rowIndex++;
        }
    }

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
                if (str.startsWith("@ExcelColumn")) {
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

    public static String buildProtoLine(String descLine, String fieldLine, int rowIndex) {
        // 从注解中获取描述
        String desc = parseFieldDesc(descLine);

        // 获取字段类型
        String type = parseFieldType(fieldLine);

        // 获取字段名
        String name = parseFieldName(fieldLine);

        StringBuilder sb = new StringBuilder();
        if (type.startsWith("List")) {
            sb.append("repeated").append(" ");
            // List<String> -> 获取具体的泛型类型
            type = type.substring(type.indexOf("<") + 1, type.indexOf(">"));
        }
        sb.append(typeMap.get(type)).append(" ");
        sb.append(name).append(" = ");
        sb.append(rowIndex).append(";");
        sb.append(" // ").append(desc).append("\r");
        System.out.println(sb);
        return sb.toString();
    }

    public static String parseFieldDesc(String content) {
        if (!content.startsWith("@ApiModelProperty")) {
            throw new RuntimeException("非法的注释");
        }
        int start = content.indexOf("\"");
        int end = content.lastIndexOf("\"");
        String desc = content.substring(start + 1, end);
        return desc;
    }

    public static String parseFieldType(String content) {
        String[] arr = content.split(" ");
        return arr[1].trim();
    }

    public static String parseFieldName(String content) {
        String[] arr = content.split(" ");
        return arr[2].trim().replaceAll(";", "");
    }


}

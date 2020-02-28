package com.coy.gupaoedu.study.demo.gencode;

import org.apache.commons.lang3.StringUtils;

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
 * 将proto文件生成java dto
 *
 * @author chenck
 * @date 2020/2/25 16:45
 */
public class Proto2JavaTest {

    public static void main(String[] args) throws FileNotFoundException {

        String filePath = "E:\\temp\\Proto2Java.txt";
        List<String> list = readFile(filePath);

        // 将message区分开来
        Map<String, List<String>> messageMap = new HashMap<>();
        List<String> message = new ArrayList<>();
        String messageName = null;
        for (String line : list) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
            if (line.startsWith("message")) {
                messageName = line.substring("message".length() + 1, line.lastIndexOf("{")).trim();
                messageMap.put(messageName, message);
            }
            if (line.startsWith("enum")) {
                messageName = line.substring("enum".length() + 1, line.lastIndexOf("{")).trim();
                messageMap.put(messageName, message);
            }
            if (line.equals("}")) {
                message.add(line);
                message = new ArrayList<>();
                continue;
            }
            message.add(line);
        }
        System.out.println(messageMap.size());

        for (Map.Entry<String, List<String>> entry : messageMap.entrySet()) {
            buildClass(entry.getValue());
        }
    }

    public static String buildClass(List<String> list) {
        StringBuilder clazz = new StringBuilder();
        for (String line : list) {
            String sb = buildLine(line);
            if (StringUtils.isBlank(sb)) {
                continue;
            }
            clazz.append(sb);
        }
        clazz.append("}");
        System.out.println(clazz.toString());
        return clazz.toString();
    }

    public static String buildLine(String line) {
        StringBuilder sb = new StringBuilder();
        if (line.startsWith("//")) {
            sb.append(line).append("\n");
        } else if (line.startsWith("message")) {
            String messageName = line.substring("message".length() + 1, line.lastIndexOf("{")).trim();
            sb.append("public class ").append(messageName).append(" {").append("\n");
        } else if (line.startsWith("string")) {
            build(sb, line, "String");
        } else if (line.startsWith("uint64")) {
            build(sb, line, "Long");
        } else if (line.startsWith("int64")) {
            build(sb, line, "Long");
        } else if (line.startsWith("uint32")) {
            build(sb, line, "Integer");
        } else if (line.startsWith("int32")) {
            build(sb, line, "Integer");
        } else if (line.startsWith("double")) {
            build(sb, line, "Double");
        } else {
            build(sb, line, "");
        }

        return sb.toString();
    }

    public static void build(StringBuilder sb, String line, String fieldType) {
        if (line.indexOf("//") != -1) {
            String desc = line.substring(line.indexOf("//"));
            sb.append(desc).append("\n");
        }
        String[] arr = line.split("=")[0].split(" ");
        if (arr.length <= 1) {
            return;
        }
        String fieldTypeTemp = arr[0];
        String field = arr[1];
        sb.append("private ");
        if (StringUtils.isNotBlank(fieldType)) {
            sb.append(fieldType).append(" ").append(field).append(";").append("\n");
        } else {
            if (fieldTypeTemp.equals("repeated")) {
                sb.append(" List<").append(field).append(">");
                sb.append(" ").append(arr[2]).append(";").append("\n");
            } else {
                sb.append(" ").append(fieldTypeTemp).append(" ").append(field).append(";").append("\n");
            }
        }
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
//                        || str.startsWith("enum")
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

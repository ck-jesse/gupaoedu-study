package com.coy.gupaoedu.study.demo.gencode;

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
 * 生成proto的字段
 *
 * @author chenck
 * @date 2020/2/6 17:09
 */
public class GeneratorProtoTest {

    public static final Map<String, String> typeMap = new HashMap<>();

    static {
        typeMap.put("String", "string");
        typeMap.put("Long", "uint64");
        typeMap.put("long", "uint64");
        typeMap.put("Integer", "uint32");
        typeMap.put("int", "uint32");
        typeMap.put("BigDecimal", "double");
        typeMap.put("Double", "double");
        typeMap.put("double", "double");
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

    /**
     * 读取文件，文件内容格式如下：
     *
     * <p>
     * @ApiModelProperty(value="主表ID")
     * private Long id;
     * @ApiModelProperty(value="工单编号")
     * private String afterSaleCode ;
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
                if (str.startsWith("@ExcelColumn")
                        || str.startsWith("@NotBlank")
                        || str.startsWith("@NotNull")
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

    /**
     * 构建proto的字段定义
     */
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
            type = type.substring(type.indexOf("<") + 1, type.lastIndexOf(">"));
        }

        // 若从定义的类型map中获取不到对应的类型，则直接使用解析出来的type，然后再认为去处理
        type = (null == typeMap.get(type) ? type :typeMap.get(type));

        sb.append(type).append(" ");
        sb.append(name).append(" = ");
        sb.append(rowIndex).append(";");
        sb.append(" // ").append(desc).append("\r");
        System.out.println(sb);
        return sb.toString();
    }

    /**
     * 解析字段描述
     */
    public static String parseFieldDesc(String content) {
        if (!content.startsWith("@ApiModelProperty")) {
            throw new RuntimeException("非法的注释: " + content);
        }
        int start = content.indexOf("\"");
        int end = content.lastIndexOf("\"");
        String desc = content.substring(start + 1, end);
        return desc;
    }

    /**
     * 解析字段类型
     */
    public static String parseFieldType(String content) {
        String[] arr = content.split(" ");
        return arr[1].trim();
    }

    /**
     * 解析字段名称
     */
    public static String parseFieldName(String content) {
        String[] arr = content.split(" ");
        return arr[2].trim().replaceAll(";", "");
    }


}

package com.coy.gupaoedu.study.mebatis.executor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chenck
 * @date 2019/5/8 20:41
 */
public class ResultSetHandler {

    public <T> List<T> handle(ResultSet resultSet, Class type) {
        List<T> list = new ArrayList<>();
        try {
            // 遍历结果集
            while (resultSet.next()) {
                // 直接调用Class的方法产生一个实例
                Object pojo = type.newInstance();
                for (Field field : pojo.getClass().getDeclaredFields()) {
                    Object value = getResult(resultSet, field);
                    setValue(pojo, field, value);
                }
                list.add((T) pojo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 通过反射给属性赋值
     */
    private void setValue(Object pojo, Field field, Object value) {
        try {
            Method setMethod = pojo.getClass().getMethod("set" + firstWordCapital(field.getName()), field.getType());
            setMethod.invoke(pojo, value);
        } catch (Exception e) {
            System.out.println(field.getName() + " 错误 " + field.getType());
            e.printStackTrace();
        }
    }

    /**
     * 根据反射判断类型，从ResultSet中取对应类型参数
     */
    private Object getResult(ResultSet rs, Field field) throws SQLException {
        Class type = field.getType();
        String dataName = HumpToUnderline(field.getName()); // 驼峰转下划线
        if (Integer.class == type) {
            return rs.getInt(dataName);
        } else if (String.class == type) {
            return rs.getString(dataName);
        } else if (Long.class == type) {
            return rs.getLong(dataName);
        } else if (Boolean.class == type) {
            return rs.getBoolean(dataName);
        } else if (Double.class == type) {
            return rs.getDouble(dataName);
        } else if (Date.class == type) {
            return rs.getDate(dataName);
        } else {
            return rs.getString(dataName);
        }
    }

    // 数据库下划线转Java驼峰命名
    public static String HumpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;
        if (!para.contains("_")) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, "_");
                    temp += 1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 单词首字母大写
     */
    private String firstWordCapital(String word) {
        String first = word.substring(0, 1);
        String tail = word.substring(1);
        return first.toUpperCase() + tail;
    }
}

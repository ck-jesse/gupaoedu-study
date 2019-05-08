package com.coy.gupaoedu.study.mebatis.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author chenck
 * @date 2019/5/8 18:47
 */
public class ParameterHandler {

    private PreparedStatement psmt;

    public ParameterHandler(PreparedStatement statement) {
        this.psmt = statement;
    }

    /**
     * 从方法中获取参数，遍历设置SQL中的？占位符
     *
     * @param parameters
     */
    public void setParameters(Object parameters) {
        try {
            if (parameters.getClass().isArray()) {
                Object[] parametersArr = (Object[]) parameters;
                // PreparedStatement的序号是从1开始的
                for (int i = 0; i < parametersArr.length; i++) {
                    int k = i + 1;
                    if (parametersArr[i] instanceof Integer) {
                        psmt.setInt(k, (Integer) parametersArr[i]);
                    } else if (parametersArr[i] instanceof Long) {
                        psmt.setLong(k, (Long) parametersArr[i]);
                    } else if (parametersArr[i] instanceof String) {
                        psmt.setString(k, String.valueOf(parametersArr[i]));
                    } else if (parametersArr[i] instanceof Boolean) {
                        psmt.setBoolean(k, (Boolean) parametersArr[i]);
                    } else {
                        psmt.setString(k, String.valueOf(parametersArr[i]));
                    }
                }
            } else if (parameters instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) parameters;
                for (Map.Entry<String, Object> entry : map.entrySet()) {

                }
            } else {
                psmt.setObject(1, parameters);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

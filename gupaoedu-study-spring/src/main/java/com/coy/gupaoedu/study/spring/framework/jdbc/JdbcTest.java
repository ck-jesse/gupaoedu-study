package com.coy.gupaoedu.study.spring.framework.jdbc;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenck
 * @date 2019/4/17 21:36
 */
public class JdbcTest {
    private static String url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8";
    private static String user = "root";
    private static String pwd = "root";

    @Test
    public void test() throws Exception {
        // 加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 获取连接
        Connection connection = DriverManager.getConnection(url, user, pwd);
        Statement statement = connection.createStatement();

        String sql = "select * from test";
        boolean result = statement.execute(sql);

        ResultSet resultSet = statement.getResultSet();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        List<Map<String, Object>> rowList = new ArrayList<>();
        while (resultSet.next()) {
            System.out.println();
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                System.out.println(metaData.getColumnName(i) + "=" + resultSet.getObject(i));
                row.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
            rowList.add(row);
        }
        statement.close();
        connection.close();

        System.out.println(rowList.toString());

    }
}

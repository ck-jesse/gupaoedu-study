package com.coy.gupaoedu.study.spring.framework.jdbc;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author chenck
 * @date 2019/4/17 21:36
 */
public class JdbcTest {
    private static String url = "";
    private static String user = "";
    private static String pwd = "";

    @Test
    public void test() throws Exception {
        // 加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 获取连接
        Connection connection = DriverManager.getConnection(url, user, pwd);
        Statement statement = connection.createStatement();

        String sql = "";
        statement.execute(sql);

        ResultSet resultSet = statement.getResultSet();

        statement.close();
        connection.close();

    }
}

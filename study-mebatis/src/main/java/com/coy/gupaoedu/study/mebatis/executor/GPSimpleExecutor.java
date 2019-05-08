package com.coy.gupaoedu.study.mebatis.executor;

import com.coy.gupaoedu.study.mebatis.GPMappedStatement;
import com.coy.gupaoedu.study.mebatis.GPResultHandler;
import com.coy.gupaoedu.study.mebatis.parameter.ParameterHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenck
 * @date 2019/5/7 17:31
 */
public class GPSimpleExecutor extends GPBaseExecutor {

    @Override
    protected <E> List<E> doQuery(GPMappedStatement ms, Object parameter, GPResultHandler resultHandler) throws SQLException {
        List<E> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            //GPConfiguration configuration = ms.getConfiguration();

            // 注册 JDBC 驱动
//            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 打开连接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
            String sql = ms.getSql();

            // 执行查询
            stmt = conn.prepareStatement(sql);

            // 参数处理
            ParameterHandler parameterHandler = new ParameterHandler(stmt);
            parameterHandler.setParameters(parameter);

            // 执行SQL
            ResultSet rs = stmt.executeQuery();

            ResultSetHandler resultSetHandler = new ResultSetHandler();
            list = resultSetHandler.handle(rs, ms.getResultType());

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            closeStatement(stmt);
        }
        return list;
    }

}

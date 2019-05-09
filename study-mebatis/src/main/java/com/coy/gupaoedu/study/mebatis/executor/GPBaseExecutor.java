package com.coy.gupaoedu.study.mebatis.executor;

import com.coy.gupaoedu.study.mebatis.GPMappedStatement;
import com.coy.gupaoedu.study.mebatis.GPResultHandler;
import com.coy.gupaoedu.study.mebatis.session.GPConfiguration;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 基础的SQL执行器
 *
 * @author chenck
 * @date 2019/5/7 15:57
 */
public abstract class GPBaseExecutor implements GPExecutor {

    protected GPConfiguration configuration;

    protected GPBaseExecutor(GPConfiguration configuration) {
        this.configuration = configuration;
    }


    @Override
    public <E> List<E> query(GPMappedStatement ms, Object parameter, GPResultHandler resultHandler) throws SQLException {
        return this.doQuery(ms, parameter, resultHandler);
    }

    protected abstract <E> List<E> doQuery(GPMappedStatement ms, Object parameter, GPResultHandler resultHandler)
            throws SQLException;

    /**
     * 关闭statement
     */
    protected void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                if (!statement.isClosed()) {
                    statement.close();
                }
            } catch (SQLException e) {
                // ignore
            }
        }
    }
}

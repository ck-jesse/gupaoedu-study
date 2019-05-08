package com.coy.gupaoedu.study.mebatis.session;

import com.coy.gupaoedu.study.mebatis.GPMappedStatement;
import com.coy.gupaoedu.study.mebatis.exception.MebatisException;
import com.coy.gupaoedu.study.mebatis.exception.TooManyResultsException;
import com.coy.gupaoedu.study.mebatis.executor.GPExecutor;

import java.sql.Connection;
import java.util.List;

/**
 * 默认的SqlSession实现
 *
 * @author chenck
 * @date 2019/5/6 20:54
 */
public class GPDefaultSqlSession implements GPSqlSession {

    private final GPConfiguration configuration;
    private final GPExecutor executor;
    private final boolean autoCommit;

    public GPDefaultSqlSession(GPConfiguration configuration, GPExecutor executor, boolean autoCommit) {
        this.configuration = configuration;
        this.executor = executor;
        this.autoCommit = autoCommit;
    }

    public GPDefaultSqlSession(GPConfiguration configuration, GPExecutor executor) {
        this(configuration, executor, false);
    }

    @Override
    public <T> T selectOne(String statement) {
        return this.<T>selectOne(statement, null);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        List<T> list = this.<T>selectList(statement, parameter);
        if (null == list || list.size() == 0) {
            return null;
        }
        if (list.size() > 1) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        }
        return list.get(0);
    }

    @Override
    public <E> List<E> selectList(String statement) {
        return this.<E>selectList(statement, null);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        try {
            // 通过statementId 获取到对应的statement
            GPMappedStatement ms = configuration.getMappedStatement(statement);
            // 通过执行器执行查询
            return executor.query(ms, parameter, GPExecutor.NO_RESULT_HANDLER);
        } catch (Exception e) {
            throw new MebatisException("Error querying database.  Cause: " + e, e);
        }
    }

    @Override
    public GPConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Connection getConnection() {
        return null;
    }

}

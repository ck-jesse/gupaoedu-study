package com.coy.gupaoedu.study.mebatis.session;

import java.sql.Connection;
import java.util.List;

/**
 * @author chenck
 * @date 2019/5/6 20:42
 */
public interface GPSqlSession {

    /**
     * 查询单条记录
     *
     * @param <T>       the returned object type
     * @param statement
     * @return Mapped object
     */
    <T> T selectOne(String statement);

    /**
     * 根据参数查询单条记录
     *
     * @param <T>       the returned object type
     * @param statement Unique identifier matching the statement to use.
     * @param parameter A parameter object to pass to the statement.
     * @return Mapped object
     */
    <T> T selectOne(String statement, Object parameter);

    /**
     * 查询多条记录.
     *
     * @param <E>       the returned list element type
     * @param statement Unique identifier matching the statement to use.
     * @return List of mapped object
     */
    <E> List<E> selectList(String statement);

    /**
     * Retrieve a list of mapped objects from the statement key and parameter.
     *
     * @param <E>       the returned list element type
     * @param statement Unique identifier matching the statement to use.
     * @param parameter A parameter object to pass to the statement.
     * @return List of mapped object
     */
    <E> List<E> selectList(String statement, Object parameter);

    /**
     * Retrieve a list of mapped objects from the statement key and parameter,
     * within the specified row bounds.
     * @param <E> the returned list element type
     * @param statement Unique identifier matching the statement to use.
     * @param parameter A parameter object to pass to the statement.
     * @param rowBounds  Bounds to limit object retrieval
     * @return List of mapped object
     */
//    <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds);

    /**
     * 获取当前全局配置对象
     *
     * @return Configuration
     */
    GPConfiguration getConfiguration();

    /**
     * 获取Mapper对象
     *
     * @param <T>  the mapper type
     * @param type Mapper interface class
     * @return a mapper bound to this SqlSession
     */
    <T> T getMapper(Class<T> type);

    /**
     * 获取数据connection
     *
     * @return Connection
     */
    Connection getConnection();
}

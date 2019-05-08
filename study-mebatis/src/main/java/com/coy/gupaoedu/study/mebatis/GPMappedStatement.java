package com.coy.gupaoedu.study.mebatis;

import lombok.Data;

import java.util.Map;

/**
 * Mapper接口中方法对应的statement
 * 注：一个方法对应一个statement
 *
 * @author chenck
 * @date 2019/5/7 16:03
 */
@Data
public class GPMappedStatement {

    /**
     * statement id
     */
    private String id;
    /**
     * 每次获取数据的数量
     */
    private Integer fetchSize;
    /**
     * 超时时间
     */
    private Integer timeout;
    /**
     * sql命令类型（增删改查）
     */
    private GPSqlCommandType sqlCommandType;
    /**
     * Statement类型（Statement, PreparedStatement）
     */
    private GPStatementType statementType;
    /**
     * 参数类型
     */
    private Class<?> parameterType;
    /**
     * 参数类型的字段map<fieldName,fieldType>
     */
    private Map<String, Class<?>> parameterTypeFieldMap;
    /**
     * 结果类型
     */
    private Class<?> resultType;
    /**
     * 结果类型的字段map<fieldName,fieldType>
     */
    private Map<String, Class<?>> resultTypeFieldMap;
    /**
     * 查询的列名
     */
    private String[] keyColumns;
    /**
     * sql语句
     */
    private String sql;

    GPMappedStatement() {
        // constructor disabled
    }

    /**
     * 构建
     */
    public static class Builder {
        private GPMappedStatement mappedStatement = new GPMappedStatement();

        public Builder(String id, GPSqlCommandType sqlCommandType) {
            mappedStatement.id = id;
            mappedStatement.statementType = GPStatementType.PREPARED;
            mappedStatement.sqlCommandType = sqlCommandType;
        }

        public String id() {
            return mappedStatement.id;
        }


        public Builder fetchSize(Integer fetchSize) {
            mappedStatement.fetchSize = fetchSize;
            return this;
        }

        public Builder timeout(Integer timeout) {
            mappedStatement.timeout = timeout;
            return this;
        }

        public Builder sqlCommandType(GPSqlCommandType sqlCommandType) {
            mappedStatement.sqlCommandType = sqlCommandType;
            return this;
        }

        public Builder statementType(GPStatementType statementType) {
            mappedStatement.statementType = statementType;
            return this;
        }

        public Builder parameterType(Class<?> parameterType) {
            mappedStatement.parameterType = parameterType;
            return this;
        }

        public Builder resultType(Class<?> resultType) {
            mappedStatement.resultType = resultType;
            return this;
        }

        public Builder sql(String sql) {
            mappedStatement.sql = sql;
            return this;
        }

        public GPMappedStatement build() {
            assert mappedStatement.id != null;
            return mappedStatement;
        }
    }
}

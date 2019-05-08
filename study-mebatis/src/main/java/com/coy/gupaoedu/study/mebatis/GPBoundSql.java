package com.coy.gupaoedu.study.mebatis;

import com.coy.gupaoedu.study.mebatis.session.GPConfiguration;

import java.util.List;

/**
 * @author chenck
 * @date 2019/5/7 16:42
 */
public class GPBoundSql {

    private GPConfiguration configuration;
    /**
     * sql语句
     */
    private final String sql;
    private final List<GPParameterMapping> parameterMappings;
    /**
     * 参数对象
     */
    private final Object parameterObject;

    public GPBoundSql(GPConfiguration configuration, String sql, List<GPParameterMapping> parameterMappings, Object parameterObject) {
        this.configuration = configuration;
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.parameterObject = parameterObject;
    }

    public String getSql() {
        return sql;
    }

    public List<GPParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public Object getParameterObject() {
        return parameterObject;
    }
}

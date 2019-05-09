package com.coy.gupaoedu.study.mebatis.session;

/**
 * @author chenck
 * @date 2019/5/9 12:42
 */
public class GPSqlSessionFactory {

    private GPConfiguration configuration;

    /**
     * build方法用于初始化Configuration，解析配置文件的工作在Configuration的构造函数中
     *
     * @return
     */
    public GPSqlSessionFactory build() {
        configuration = new GPConfiguration();
        return this;
    }

    /**
     * 获取DefaultSqlSession
     *
     * @return
     */
    public GPDefaultSqlSession openSqlSession(boolean autoCommit) {
        return new GPDefaultSqlSession(configuration, autoCommit);
    }

    public GPDefaultSqlSession openSqlSession() {
        return openSqlSession(false);
    }
}

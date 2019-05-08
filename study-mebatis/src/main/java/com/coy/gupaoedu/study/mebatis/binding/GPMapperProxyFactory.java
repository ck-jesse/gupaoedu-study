package com.coy.gupaoedu.study.mebatis.binding;

import com.coy.gupaoedu.study.mebatis.session.GPSqlSession;

import java.lang.reflect.Proxy;

/**
 * @author chenck
 * @date 2019/5/7 18:05
 */
public class GPMapperProxyFactory<T> {
    private Class<T> mapperInterface;

    public GPMapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public T newInstance(GPSqlSession sqlSession) {
        // 创建MapperProxy代理对象
        final GPMapperProxy<T> mapperProxy = new GPMapperProxy<T>(sqlSession, mapperInterface);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }
}

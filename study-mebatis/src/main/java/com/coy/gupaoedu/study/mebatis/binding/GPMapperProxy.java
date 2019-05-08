package com.coy.gupaoedu.study.mebatis.binding;

import com.coy.gupaoedu.study.mebatis.session.GPSqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mapper代理对象：Mapper接口对应的代理对象
 * Mapper接口的作用仅仅是为了找到对应的statement，所以Mapper接口无需实现类
 *
 * @author chenck
 * @date 2019/5/6 20:43
 */
public class GPMapperProxy<T> implements InvocationHandler, Serializable {

    private final GPSqlSession sqlSession;
    private final Class<T> mapperInterface;

    public GPMapperProxy(GPSqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String statementId = getStatementId(method);

        Object result = sqlSession.selectOne(statementId, args);
        return result;
    }

    /**
     * 通过method获取对应的statementId，也就是MapperMethod
     */
    public String getStatementId(Method method) {
        String statementId = mapperInterface.getName() + "." + method.getName();
        return statementId;
    }
}

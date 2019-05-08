package com.coy.gupaoedu.study.mebatis.binding;

import com.coy.gupaoedu.study.mebatis.exception.BindingException;
import com.coy.gupaoedu.study.mebatis.session.GPConfiguration;
import com.coy.gupaoedu.study.mebatis.session.GPSqlSession;

import java.util.HashMap;
import java.util.Map;

/**
 * Mapper接口的注册器
 *
 * @author chenck
 * @date 2019/5/7 15:05
 */
public class GPMapperRegistry {

    private final GPConfiguration config;

    /**
     * Mapper接口与代理对象工厂的容器
     */
    private final Map<Class<?>, GPMapperProxyFactory<?>> knownMappers = new HashMap<Class<?>, GPMapperProxyFactory<?>>();

    public GPMapperRegistry(GPConfiguration config) {
        this.config = config;
    }

    /**
     * 获取Mapper接口的代理对象
     * 注：
     */
    public <T> T getMapper(Class<T> type, GPSqlSession sqlSession) {
        final GPMapperProxyFactory<T> mapperProxyFactory = (GPMapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new BindingException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    /**
     * 判断Mapper接口是否已经注册
     */
    public <T> boolean hasMapper(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    /**
     * 注册Mapper接口
     */
    public <T> void addMapper(Class<T> type) {
        if (type.isInterface()) {
            if (hasMapper(type)) {
                throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
            }
            boolean loadCompleted = false;
            try {
                knownMappers.put(type, new GPMapperProxyFactory<>(type));
                // 此处可对Mapper接口中定义了对应注解的方法进行解析处理
                loadCompleted = true;
            } finally {
                if (!loadCompleted) {
                    knownMappers.remove(type);
                }
            }
        }
    }

}

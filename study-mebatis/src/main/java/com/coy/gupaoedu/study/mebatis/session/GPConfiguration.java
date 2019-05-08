package com.coy.gupaoedu.study.mebatis.session;

import com.coy.gupaoedu.study.mebatis.ClassUtil;
import com.coy.gupaoedu.study.mebatis.GPMappedStatement;
import com.coy.gupaoedu.study.mebatis.PropertiesUtils;
import com.coy.gupaoedu.study.mebatis.binding.GPMapperRegistry;
import com.coy.gupaoedu.study.mebatis.build.XMLMapperBuilder;
import com.coy.gupaoedu.study.mebatis.exception.MebatisException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局配置对象
 *
 * @author chenck
 * @date 2019/5/6 20:43
 */
public class GPConfiguration {

    private static final String DEFAULT_CONFIG_FILE = "mebatis-config.properties";

    private String configFileName = DEFAULT_CONFIG_FILE;

    /**
     * Mapper接口的注册器
     */
    protected final GPMapperRegistry mapperRegistry = new GPMapperRegistry(this);

    /**
     * 存放Mapper接口中方法对应的statement<statementId,MapperStatement>
     * 注：一个方法对应一个statement
     */
    protected final Map<String, GPMappedStatement> mappedStatements = new StrictMap<GPMappedStatement>("Mapped Statements collection");


    public GPConfiguration() {
        this(DEFAULT_CONFIG_FILE);
    }

    public GPConfiguration(String configFileName) {
        this.configFileName = configFileName;

        // 加载属性文件
        PropertiesUtils.load(configFileName);

        // Mapper接口注册
        mapperRegistry(PropertiesUtils.getMapperPackagePath());

        // Mapper映射器解析（sql statement解析）
        mapperStatementParse(PropertiesUtils.getMapperSqlXmlResouce());

    }

    /**
     * Mapper接口注册
     */
    protected void mapperRegistry(String mapperPackagePath) {
        try {
            // 获取Mapper接口列表
            List<Class<?>> mapperClassList = ClassUtil.scannerPackage(mapperPackagePath);
            if (null == mapperClassList || mapperClassList.size() == 0) {
                return;
            }
            for (Class<?> mapperClass : mapperClassList) {
                mapperRegistry.addMapper(mapperClass);
            }
        } catch (Exception e) {
            throw new MebatisException("Mapper接口注册异常", e);
        }
    }

    /**
     * Mapper映射器解析
     */
    public void mapperStatementParse(String mapperXmlResource) {
        XMLMapperBuilder xmlConfigBuilder = new XMLMapperBuilder(this, mapperXmlResource);
        xmlConfigBuilder.parse();
    }

    /**
     * 获取Mapper接口的代理对象
     */
    public <T> T getMapper(Class<T> type, GPSqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    /**
     * 获取statement
     */
    public GPMappedStatement getMappedStatement(String statementId) {
        return mappedStatements.get(statementId);
    }

    /**
     * 添加statement
     */
    public void addMappedStatement(GPMappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    /**
     * 扩展HashMap，支持设置名称
     */
    protected static class StrictMap<V> extends HashMap<String, V> {
        private final String name;

        public StrictMap(String name, int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
            this.name = name;
        }

        public StrictMap(String name, int initialCapacity) {
            super(initialCapacity);
            this.name = name;
        }

        public StrictMap(String name) {
            super();
            this.name = name;
        }

        public StrictMap(String name, Map<String, ? extends V> m) {
            super(m);
            this.name = name;
        }

        public V put(String key, V value) {
            if (containsKey(key)) {
                throw new IllegalArgumentException(name + " already contains value for " + key);
            }
            return super.put(key, value);
        }

        public V get(Object key) {
            V value = super.get(key);
            if (value == null) {
                throw new IllegalArgumentException(name + " does not contain value for " + key);
            }
            return value;
        }

    }
}

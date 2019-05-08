package com.coy.gupaoedu.study.mebatis;

import com.coy.gupaoedu.study.mebatis.session.GPConfiguration;

import java.util.Collections;
import java.util.List;

/**
 * 映射文件中的参数Map定义
 *
 * @author chenck
 * @date 2019/5/7 17:04
 */
public class GPParameterMap {

    private String id;
    private Class<?> type;
    private List<GPParameterMapping> parameterMappings;

    private GPParameterMap() {
    }

    public static class Builder {
        private GPParameterMap parameterMap = new GPParameterMap();

        public Builder(GPConfiguration configuration, String id, Class<?> type, List<GPParameterMapping> parameterMappings) {
            parameterMap.id = id;
            parameterMap.type = type;
            parameterMap.parameterMappings = parameterMappings;
        }

        public Class<?> type() {
            return parameterMap.type;
        }

        public GPParameterMap build() {
            //lock down collections
            parameterMap.parameterMappings = Collections.unmodifiableList(parameterMap.parameterMappings);
            return parameterMap;
        }
    }

    public String getId() {
        return id;
    }

    public Class<?> getType() {
        return type;
    }

    public List<GPParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

}

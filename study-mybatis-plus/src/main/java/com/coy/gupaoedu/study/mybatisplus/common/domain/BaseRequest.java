package com.coy.gupaoedu.study.mybatisplus.common.domain;

import java.io.Serializable;

/**
 * 基础的请求入参
 * <p>
 * 注意：具体的业务接口的入参对象在定义时，可以继承该类，也可以不继承，只需要保持命名规范即可
 *
 * @author chenck
 * @date 2019/9/5 10:36
 */
public class BaseRequest implements Serializable {

    private static final long serialVersionUID = 1717442845820713651L;

    /**
     * trace_id
     */
    private String traceId;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}

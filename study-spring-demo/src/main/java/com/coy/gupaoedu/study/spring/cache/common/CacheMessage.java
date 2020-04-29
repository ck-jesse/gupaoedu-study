package com.coy.gupaoedu.study.spring.cache.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author chenck
 * @date 2020/4/28 20:29
 */
@Getter
@Setter
public class CacheMessage implements Serializable {

    private static final long serialVersionUID = 2103077364243459916L;

    private String cacheName;// 缓存名称
    private Object key;// 缓存key

    public CacheMessage(String cacheName, Object key) {
        super();
        this.cacheName = cacheName;
        this.key = key;
    }

}

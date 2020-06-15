package com.coy.gupaoedu.study.spring.circularreference;

import com.hs.platform.log.access.annotation.LogAccess;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenck
 * @date 2020/6/12 12:17
 */
@LogAccess
//@Component
public class ServiceA {

    private final Map<Object, Object> earlyProxyReferences = new ConcurrentHashMap<>(16);
    @Autowired
    ServiceB serviceB;

    public void print() {
        System.out.println("ServiceA");
    }

    public void put(String key, String value) {
        System.out.println(earlyProxyReferences.size());
        earlyProxyReferences.put(key, value);
        System.out.println(earlyProxyReferences.size());
    }

    public static void main(String[] args) {
        ServiceA serviceA = new ServiceA();
        serviceA.put("key","value");
    }
}

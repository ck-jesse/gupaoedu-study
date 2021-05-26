package com.coy.gupaoedu.study.spring.clazz;

import com.coy.gupaoedu.study.spring.cache.common.ExtendCache;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author chenck
 * @date 2021/5/24 19:41
 */
public class ClassUtil {
    /**
     * 获取指定Class所有子类
     *
     * @param clazz
     * @return
     * @author chenck
     * @date 2018/8/23 19:16
     */
    public static List<Class> getAllSubClass(Class clazz) {
        String packageName = clazz.getPackage().getName();
        Reflections reflections = new Reflections(packageName);
        Set<Class> subClassSet = reflections.getSubTypesOf(clazz);

        List<Class> subClassList = new ArrayList<>();
        for (Class subClass : subClassSet) {
            subClassList.add(subClass);
        }
        return subClassList;
    }

    public static void main(String[] args) {
        // 只能获取ExtendCache所在package下的子类
        List<Class> strategyClassList = ClassUtil.getAllSubClass(ExtendCache.class);
        System.out.println(strategyClassList);
    }
}

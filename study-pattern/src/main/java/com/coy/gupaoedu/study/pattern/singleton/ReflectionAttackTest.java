package com.coy.gupaoedu.study.pattern.singleton;

import org.junit.Test;

import java.lang.reflect.Constructor;

/**
 * 反射攻击测试
 * <p>
 * 防止反射攻击解决方案：在构造函数中判断单例对象是否为null，若不为null，则看作攻击，直接抛出异常
 *
 * @author chenck
 * @date 2019/3/20 10:54
 */
public class ReflectionAttackTest {

    @Test
    public void eagerSingletonTest() throws Exception {
        // 正常情况下获取单例
        EagerSingleton instance1 = EagerSingleton.getInstance();
        EagerSingleton instance2 = EagerSingleton.getInstance();

        // 通过反射攻击获取单例
        Constructor<EagerSingleton> constructor = EagerSingleton.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        EagerSingleton instanceReflection = constructor.newInstance();

        System.out.println(instance1 + "\n" + instance2 + "\n" + instanceReflection);
        System.out.println("正常情况下，实例化两个实例是否相同：" + (instance1 == instance2));
        System.out.println("通过反射攻击单例模式情况下，实例化两个实例是否相同：" + (instance1 == instanceReflection));

    }
}

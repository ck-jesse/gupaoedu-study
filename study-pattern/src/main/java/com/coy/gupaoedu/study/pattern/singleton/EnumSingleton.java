package com.coy.gupaoedu.study.pattern.singleton;

/**
 * 枚举单例模式
 * 注：单元素的枚举类型为实现单例模式的最佳方法
 * <p>
 * 代码极其简洁，无偿地提供了序列化机制，绝对防止对此实例化，即使是在面对复杂的序列化或者反射攻击的时候。
 * <p>
 * 特点：线程安全、防反射攻击、防止序列化生成新的实例。
 *
 * @author chenck
 * @date 2019/3/20 11:08
 */
public enum EnumSingleton {
    INSTANCE;

    private String name;

    public void test() {
        System.out.println("The Test!");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

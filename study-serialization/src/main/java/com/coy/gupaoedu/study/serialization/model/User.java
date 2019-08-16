package com.coy.gupaoedu.study.serialization.model;

import java.io.IOException;
import java.io.Serializable;

/**
 * Serializable接口它没有任何方法，在 Java 中也称为标记接口。
 * 当类实现 java.io.Serializable 接口时, 它将在 Java 中变得可序列化, 并指示编译器使用 Java 序列化机制序列化此对象。
 *
 * @author chenck
 * @date 2019/6/3 11:46
 */
public class User implements Serializable {
    /**
     * Java 的序列化机制是通过判断类的 serialVersionUID 来验证版本一致性的。
     * serialVersionUID 用于对象的版本控制。
     * <p>
     * 不指定 serialVersionUID 的后果是,当你添加或修改类中的任何字段时, 则已序列化类将无法恢复, 因为为新类和旧序列化对象生成的 serialVersionUID 将有所不同。
     * Java 序列化过程依赖于正确的序列化对象恢复状态的，并在序列化对象序列版本不匹配的情况下引发 java.io.InvalidClassException 无效类异常
     */
    private static final long serialVersionUID = -6813325728666777435L;

    private String name;
    private Integer age;

    /**
     * 静态变量不会被序列化
     * 注：由于静态变量属于类, 而不是对象, 因此它们不是对象状态的一部分, 因此在 Java 序列化过程中不会保存它们。
     */
    private static String type = "female";

    /**
     * transient 瞬态变量
     * transient 阻止address字段被序列化
     * <p>
     * 但是可以通过重写writeObject和readObject方法来使得address字段被正确的序列化
     * <p>
     * writeObject和readObject方法是ObjectInputStream 和 ObjectOutputStream中通过反射来调用的
     */
    private transient String address;


    /**
     * 序列化一个对象调用 ObjectOutputStream.writeObject(saveThisObject),
     * 反序列化一个对象调用 ObjectInputStream.readObject()
     * 但 Java 虚拟机为你提供的还有一件事, 是定义这两个方法。如果在类中定义这两种方法, 则 JVM 将调用这两种方法, 而不是应用默认序列化机制。
     * 你可以在类中定义 writeObject 和 readObject 来执行任何类型的预处理或后处理任务来自定义对象序列化和反序列化的行为。
     *
     * 注：需要注意的非常重要一点是要声明这些方法为私有方法, 以避免被继承、重写或重载。
     * 由于只有 Java 虚拟机可以调用类的私有方法, 你的类的完整性会得到保留, 并且 Java 序列化将正常工作。
     */

    /**
     * 重写 writeObject() 方法来自定义序列化行为
     */
    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(name);
    }

    /**
     * 重写 readObject() 方法来自定义反序列化行为
     */
    private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        name = (String) s.readObject();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

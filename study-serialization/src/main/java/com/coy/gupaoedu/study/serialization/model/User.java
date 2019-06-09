package com.coy.gupaoedu.study.serialization.model;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author chenck
 * @date 2019/6/3 11:46
 */
public class User implements Serializable {
    /**
     * Java 的序列化机制是通过判断类的 serialVersionUID 来验证版本一致性的。
     */
    private static final long serialVersionUID = -6813325728666777435L;

    private String name;
    private Integer age;

    /**
     * transient 阻止address字段被序列化
     * <p>
     * 但是可以通过重写writeObject和readObject方法来使得address字段被正确的序列化
     * <p>
     * writeObject和readObject方法是ObjectInputStream 和 ObjectOutputStream中通过反射来调用的
     */
    private transient String address;

    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(name);
    }

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

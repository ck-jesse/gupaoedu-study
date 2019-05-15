package com.coy.gupaoedu.study.pattern.singleton;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化攻击（序列化生成新的实例）
 * <p>
 * 在单例类中添加readResolve方法来防止反序列化创建新的实例
 *
 * @author chenck
 * @date 2019/3/20 10:57
 */
public class SerializeAttackTest {

    @Test
    public void eagerSingletonTest() throws Exception {
        // 获取单例
        EagerSingleton instance1 = EagerSingleton.getInstance();
        instance1.setContent("单例序列化");
        System.out.println("序列化前读取其中的内容：" + instance1.getContent());
        System.out.println("序列化前：" + instance1);
        System.out.println();

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("EagerSingleton.obj"));
        oos.writeObject(instance1);
        oos.flush();
        oos.close();

        FileInputStream fis = new FileInputStream("EagerSingleton.obj");
        ObjectInputStream ois = new ObjectInputStream(fis);
        // 分析：任何一个readObject方法，不管是显式的还是默认的，它都会返回一个新建的实例，这个新建的实例不同于该类初始化时创建的实例。
        // 解决：在单例类中添加readResolve方法来防止反序列化创建新的实例
        EagerSingleton instance2 = (EagerSingleton) ois.readObject();
        ois.close();

        System.out.println("序列化后：" + instance2);
        System.out.println("序列化后读取其中的内容：" + instance2.getContent());
        System.out.println();
        System.out.println("序列化前后两个是否同一个：" + (instance1 == instance2));

    }
}

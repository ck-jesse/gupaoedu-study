package com.coy.gupaoedu.study.pattern.singleton;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;

/**
 * 枚举单例模式测试
 * <p>
 * javap 命令查看编译后的代码（即查看字节码文件）
 * javap 是jdk自带的反解析工具。它的作用就是根据class字节码文件，反解析出当前类对应的code区（汇编指令）、本地变量表、异常表和代码行偏移量映射表、常量池等等信息。
 *
 * @author chenck
 * @date 2019/3/20 11:20
 */
public class EnumSingletonTest {

    @Test
    public void enumSingletonTest() {
        EnumSingleton instance1 = EnumSingleton.INSTANCE;
        EnumSingleton instance2 = EnumSingleton.INSTANCE;

        instance1.setName("I am a EnumSingleton Instance");
        System.out.println("instanse1：" + instance1.getName());
        System.out.println("instanse2：" + instance2.getName());

        System.out.println("正常情况下，实例化两个实例是否相同：" + (instance1 == instance2));
    }

    /**
     * 枚举单例的反射攻击
     * 分析：枚举类实际为抽象类，同时在反射时，有判断类是否有ENUM修饰，如果是则抛出异常，所以反射失败，也就达到了防止反射的目的
     */
    @Test
    public void reflectionAttackTest1() throws Exception {
        // 正常情况下获取单例
        EnumSingleton instance1 = EnumSingleton.INSTANCE;
        EnumSingleton instance2 = EnumSingleton.INSTANCE;
        System.out.println("instance1=" + instance1);
        System.out.println("instance2=" + instance2);
        System.out.println("正常情况下，实例化两个实例是否相同：" + (instance1 == instance2));

        // 通过反射攻击获取单例
        // 下面会报java.lang.NoSuchMethodException，通过javap -p EnumSingleton.class查看该枚举类源码发现：
        // 枚举Enum是个抽象类（final），且其中的成员 INSTANCE 是static类型的，所以会在类被加载之后被初始化（Java类的加载和初始化过程都是线程安全的）
        // 其实一旦一个类声明为枚举，实际上就是继承了Enum，所以会有（String.class,int.class）的构造器。
        Constructor<EnumSingleton> constructor = EnumSingleton.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        EnumSingleton instanceReflection = constructor.newInstance();

        System.out.println("instanceReflection=" + instanceReflection);
        System.out.println("通过反射攻击单例模式情况下，实例化两个实例是否相同：" + (instance1 == instanceReflection));
    }

    @Test
    public void reflectionAttackTest2() throws Exception {
        // 正常情况下获取单例
        EnumSingleton instance1 = EnumSingleton.INSTANCE;
        EnumSingleton instance2 = EnumSingleton.INSTANCE;
        System.out.println("instance1=" + instance1);
        System.out.println("instance2=" + instance2);
        System.out.println("正常情况下，实例化两个实例是否相同：" + (instance1 == instance2));

        // 通过反射攻击获取单例
        // 调用其父类的构造器
        Constructor<EnumSingleton> constructor = EnumSingleton.class.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);
        // newInstance()的时候会报Cannot reflectively create enum objects
        // 查看newInstance()的源码发现：
        // 检查该类是否ENUM修饰，如果是则抛出异常，反射失败。
        EnumSingleton instanceReflection = constructor.newInstance();

        System.out.println("instanceReflection=" + instanceReflection);
        System.out.println("通过反射攻击单例模式情况下，实例化两个实例是否相同：" + (instance1 == instanceReflection));
    }

    /**
     * 枚举单例的反序列化创建实例
     * 分析：在序列化的时候Java仅仅是将枚举对象的name属性输出到结果中，反序列化的时候则是通过java.lang.Enum的valueOf方法来根据名字查找枚举对象。
     * 同时，编译器是不允许任何对这种序列化机制的定制的，因此禁用了writeObject、readObject、readObjectNoData、writeReplace和readResolve等方法。
     */
    @Test
    public void serializeAttackTest() throws Exception {
        // 正常情况下获取单例
        EnumSingleton instance1 = EnumSingleton.INSTANCE;
        instance1.setName("单例序列化");
        System.out.println("序列化前读取其中的内容：" + instance1.getName());
        System.out.println("序列化前：" + instance1);
        System.out.println();

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("EnumSingleton.obj"));
        oos.writeObject(instance1);
        oos.flush();
        oos.close();

        FileInputStream fis = new FileInputStream("EnumSingleton.obj");
        ObjectInputStream ois = new ObjectInputStream(fis);
        // 任何一个readObject方法，不管是显式的还是默认的，它都会返回一个新建的实例，这个新建的实例不同于该类初始化时创建的实例。
        EnumSingleton instance2 = (EnumSingleton) ois.readObject();
        ois.close();

        System.out.println("序列化后：" + instance2);
        System.out.println("序列化后读取其中的内容：" + instance2.getName());
        System.out.println();
        System.out.println("序列化前后两个是否同一个：" + (instance1 == instance2));
    }
}

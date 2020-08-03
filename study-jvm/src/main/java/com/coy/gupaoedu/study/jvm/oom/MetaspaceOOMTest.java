package com.coy.gupaoedu.study.jvm.oom;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法区溢出（非堆内存）
 * <p>
 *
 * @author chenck
 * @date 2020/8/3 16:22
 */
public class MetaspaceOOMTest extends ClassLoader {

    List<Class<?>> list = new ArrayList<>();

    public void add(Class<?> clazz) {
        list.add(clazz);
    }

    public void addAll(List<Class<?>> classes) {
        list.addAll(classes);
    }

    /**
     * 设置Metaspace大小： -XX:MetaspaceSize=50M -XX:MaxMetaspaceSize=50M -XX:+PrintGCDetails -XX:+PrintGCDateStamps
     * 异常：java.lang.OutOfMemoryError: Metaspace
     */
    public static void main(String[] args) throws InterruptedException {
        MetaspaceOOMTest test = new MetaspaceOOMTest();
        while (true) {
            test.addAll(MetaspaceOOMTest.createClasses());
            Thread.sleep(20);
        }
    }

    /**
     * 基于asm创建Class对象
     */
    public static List<Class<?>> createClasses() {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (int i = 0; i < 10000000; ++i) {
            ClassWriter cw = new ClassWriter(0);
            cw.visit(Opcodes.V1_1, Opcodes.ACC_PUBLIC, "Class" + i, null, "java/lang/Object", null);
            MethodVisitor mw = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
            mw.visitVarInsn(Opcodes.ALOAD, 0);
            mw.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
            mw.visitInsn(Opcodes.RETURN);
            mw.visitMaxs(1, 1);
            mw.visitEnd();
            byte[] code = cw.toByteArray();
            MetaspaceOOMTest test = new MetaspaceOOMTest();
            Class<?> exampleClass = test.defineClass("Class" + i, code, 0, code.length);
            classes.add(exampleClass);
        }
        return classes;
    }
}

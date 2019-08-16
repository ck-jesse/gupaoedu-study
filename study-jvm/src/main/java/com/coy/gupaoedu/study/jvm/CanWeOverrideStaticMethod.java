package com.coy.gupaoedu.study.jvm;

/**
 * 问：你能用Java覆盖静态方法吗？如果我在子类中创建相同的方法是编译时错误？
 * 答：不，你不能在Java中覆盖静态方法，但在子类中声明一个完全相同的方法不是编译时错误，这称为隐藏在Java中的方法。
 * <p>
 * Java program which demonstrate that we can not override static method in Java.
 * Had Static method can be overridden, with Super class type and sub class object
 * static method from sub class would be called in our example, which is not the case.
 */
public class CanWeOverrideStaticMethod {

    public static void main(String args[]) {

        Screen scrn = new ColorScreen();

        //if we can  override static , this should call method from Child class
        scrn.show(); //IDE will show warning, static method should be called from classname

        Screen.show();

        ColorScreen colorScreen = new ColorScreen();
        colorScreen.show();// 此处show()是一个隐藏方法，通过colorScreen.是不会提示show()的，所以是一个隐藏方法;

        ColorScreen.show();

    }

}

class Screen {
    /**
     * public static method which can not be overridden in Java
     */
    public static void show() {
        System.out.println("Static method from parent class");
    }
}

class ColorScreen extends Screen {
    /**
     * 子类中的静态方法为一个隐藏的方法，通过
     * static method of same name and method signature as existed in super
     * class, this is not method overriding instead this is called
     * method hiding in Java
     */
    public static void show() {
        System.err.println("Overridden static method in Child Class in Java");
    }
}


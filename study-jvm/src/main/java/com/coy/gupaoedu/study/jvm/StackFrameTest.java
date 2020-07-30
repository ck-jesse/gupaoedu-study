package com.coy.gupaoedu.study.jvm;

/**
 * 编译：javac StackFrameTest.java
 * 反编译：javap -p -v StackFrameTest.class
 *
 * @author chenck
 * @date 2020/7/30 10:01
 */
public class StackFrameTest {

    public static void main(String[] args) {
        add(5, 7);
    }

    private static int add(int a, int b) {
        int c = 0;
        c = a + b;
        return c;
    }
}
/*
// javap -p -v StackFrameTest.class
Classfile /E:/gitee/gupaoedu-study/study-jvm/src/main/java/com/coy/gupaoedu/study/jvm/StackFrameTest.class
  Last modified 2020-7-30; size 389 bytes
  MD5 checksum 486755e2bd91d5cde37ce99b7fc69a82
  Compiled from "StackFrameTest.java"
public class com.coy.gupaoedu.study.jvm.StackFrameTest
  minor version: 0                        // JDK 最低版本号
  major version: 52                       // JDK 最高版本号
  flags: ACC_PUBLIC, ACC_SUPER            // 访问标志
Constant pool:                            // 常量池
   #1 = Methodref          #4.#15         // java/lang/Object."<init>":()V
   #2 = Methodref          #3.#16         // com/coy/gupaoedu/study/jvm/StackFrameTest.add:(II)I
   #3 = Class              #17            // com/coy/gupaoedu/study/jvm/StackFrameTest
   #4 = Class              #18            // java/lang/Object
   #5 = Utf8               <init>
   #6 = Utf8               ()V
   #7 = Utf8               Code
   #8 = Utf8               LineNumberTable
   #9 = Utf8               main
  #10 = Utf8               ([Ljava/lang/String;)V
  #11 = Utf8               add
  #12 = Utf8               (II)I
  #13 = Utf8               SourceFile
  #14 = Utf8               StackFrameTest.java
  #15 = NameAndType        #5:#6          // "<init>":()V
  #16 = NameAndType        #11:#12        // add:(II)I
  #17 = Utf8               com/coy/gupaoedu/study/jvm/StackFrameTest
  #18 = Utf8               java/lang/Object
{
  // 默认的构造函数
  public com.coy.gupaoedu.study.jvm.StackFrameTest();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 10: 0

  public static void main(java.lang.String[]);
    // 方法描述
    // 括号内表示入参类型。即 [Ljava/lang/String; 表示 String 数组参数
    // 括号外表示返回类型。即 V 表示返回 void 类型
    descriptor: ([Ljava/lang/String;)V
    // 访问标志：这里表示公共静态方法
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      // 操作数栈为2
      // 本地变量数为1
      // 入参个数为1
      stack=2, locals=1, args_size=1
         0: iconst_5                // 将int类型常量5压入[操作数栈]
         1: bipush        7         // 将int类型常量7压入[操作数栈]
         3: invokestatic  #2        // 执行静态方法 Method add:(II)I，对应到常量池中的 #2 位置
         6: pop                     //
         7: return                  // 执行完以后返回void
      // 行号表，表示代码行号与字节码行号的对应关系
      LineNumberTable:
        line 13: 0      // 表示第13行代码，对应的指令为0
        line 14: 7      // 表示第14行代码，对应的指令为7

  private static int add(int, int);
    // 方法描述
    // 括号内表示入参类型。即 (II) 表示两个 int 类型参数
    // 括号外表示返回类型。即 I 表示返回 int 类型
    descriptor: (II)I
    // 访问标志：这里表示私有静态方法
    flags: ACC_PRIVATE, ACC_STATIC
    // 代码块
    Code:
      // 操作数栈为2
      // 本地变量数为3
      // 入参个数为2
      stack=2, locals=3, args_size=2
         0: iconst_0    // 将int类型常量0压入[操作数栈]
         1: istore_2    // 将[操作数栈]栈顶元素取出，并保存到[局部变量2]
         2: iload_0     // 从[局部变量0]中装载值压入[操作数栈]
         3: iload_1     // 从[局部变量1]中装载值压入[操作数栈]
         4: iadd        // 将[操作数栈]栈顶元素取出，执行int类型的加法，结果压入[操作数栈]
         5: istore_2    // 将[操作数栈]栈顶元素保存到[局部变量2]
         6: iload_2     // 从[局部变量2]中装载值压入[操作数栈]
         7: ireturn     // 返回[操作数栈]栈顶元素
      // 行号表，表示代码行号与字节码行号的对应关系
      LineNumberTable:
        line 17: 0      // 表示第17行代码，对应的指令为0
        line 18: 2      // 表示第18行代码，对应的指令为2
        line 19: 6      // 表示第19行代码，对应的指令为6
}
SourceFile: "StackFrameTest.java"
*/
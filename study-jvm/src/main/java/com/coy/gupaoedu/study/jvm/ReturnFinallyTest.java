package com.coy.gupaoedu.study.jvm;

public class ReturnFinallyTest {

    public static void main(String[] args) {
        System.out.println(test());
    }

    /**
     * 验证 return 之前 finally 语句块中的赋值操作，最终返回的值是什么？
     */
    public static String test() {
        String value = "value";
        try {
            // 为什么返回的是 value，而不是 value1 呢？
            // 通过javap反编译工具查看字节码内容，分析可得知
            // 1、return前执行了finally语句块，局部变量表[0]=value
            // 2、在finally语句块执行前，先将value值存放在局部变量表[1]的位置，相当于是一个副本
            // 3、finally语句块将局部变量表[0]=value的值替换为了value1
            // 4、return的值是取局部变量表[1]=value来返回的
            return value;
        } finally {
            value = "value1";
        }
    }
}
/*
 // javap -v ReturnFinallyTest.class
 // test()方法的字节码内容
  public static java.lang.String test();
    descriptor: ()Ljava/lang/String;
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=1, locals=3, args_size=0
         0: ldc           #5         // 访问运行时常量池中的值 value，压入[操作数栈]  -> 栈顶元素=value
         2: astore_0                 // 将[操作数栈]栈顶元素取出，并保存到[局部变量0]  -> 将栈顶元素=value存入局部变量表[0]=value
         3: aload_0                  // 从[局部变量0]中装载值压入[操作数栈]            -> 栈顶为value
         4: astore_1                 // 将[操作数栈]栈顶元素取出，并保存到[局部变量1]   -> 将栈顶元素=value存入局部变量表[1]=value
         5: ldc           #6         // 访问运行时常量池中的值 value1，压入[操作数栈]   -> 栈顶元素=value1
         7: astore_0                 // 将[操作数栈]栈顶元素取出，并保存到[局部变量0]    -> 将栈顶元素=value1存入局部变量表[0]=value1，替换旧值
         8: aload_1                  // 从[局部变量1]中装载值压入[操作数栈]             -> 将局部变量表[1]=value压入栈顶
         9: areturn                  // 返回栈顶元素                                  -> 返回value
        10: astore_2                 // 将[操作数栈]栈顶元素取出，并保存到[局部变量2]
        11: ldc           #6         // 访问运行时常量池中的值 value1，压入[操作数栈]
        13: astore_0                 // 将[操作数栈]栈顶元素取出，并保存到[局部变量0]
        14: aload_2                  // 从[局部变量2]中装载值压入[操作数栈]
        15: athrow                   //
      Exception table:
         from    to  target type
             3     5    10   any
      // 行号表，表示代码行号与字节码行号的对应关系
      LineNumberTable:
        line 17: 0
        line 21: 3
        line 23: 5
        line 21: 8
        line 23: 10
        line 24: 14
      // 本地变量表，以Slot为单位，可以简单理解为数组
      LocalVariableTable:
        // Slot
        Start  Length  Slot  Name   Signature
            3      13     0 value   Ljava/lang/String;
      StackMapTable: number_of_entries = 1
        frame_type = 255  full_frame
          offset_delta = 10
          locals = [ class java/lang/String ]
          stack = [ class java/lang/Throwable ]
*/
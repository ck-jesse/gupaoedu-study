# 1、问题

首先上问题，具体如下：

1、return 和 finally 语句块执行先后顺序？

2、return 返回的值是执行 finally语句块 之前的值，还是 finally语句块 中赋值后的值？



# 2、源码

```java
public class ReturnFinallyTest {

    public static void main(String[] args) {
        System.out.println(test());// 打印 value
    }

    public static String test() {
        String value = "value";
        try {
            return value;
        } finally {
            value = "value1";
        }
    }
}
```



# 3、分析

javap 反编译得到字节码文件。

```shell
javap -v ReturnFinallyTest.class
```

下面只截取关键的部分来分析，具体如下：

```
public static java.lang.String test();
descriptor: ()Ljava/lang/String;
flags: ACC_PUBLIC, ACC_STATIC
Code:
  stack=1, locals=3, args_size=0     
	 0: ldc           #5 	(1)
	 2: astore_0        	(2)
	 3: aload_0          	(3)
	 4: astore_1         	(4) 将旧值value存入局部变量表[1]，相当于副本
	 5: ldc           #6 	(5)
	 7: astore_0         	(6) 将新值value1存入局部变量表[0]，替换旧值
	 8: aload_1          	(7) 从局部变量表[1]取出旧值value并压栈
	 9: areturn         	(8) 返回栈顶元素value
	10: astore_2        
	11: ldc           #6 
	13: astore_0         
	14: aload_2          
	15: athrow           
  Exception table:
	 from    to  target type
		 3     5    10   any
```

(1)访问运行时常量池中的值 value，压入[操作数栈]

> 栈顶元素=value

(2)将[操作数栈]栈顶元素取出，并保存到[局部变量0] 

> 将栈顶元素=value存入局部变量表[0]=value

(3)从[局部变量0]中装载值压入[操作数栈]

> 栈顶为value

(4)将[操作数栈]栈顶元素取出，并保存到[局部变量1] 

> 将栈顶元素=value存入局部变量表[1]=value

(5)访问运行时常量池中的值 value1，压入[操作数栈]

> 栈顶元素=value1

(6)将[操作数栈]栈顶元素取出，并保存到[局部变量0]

> 将栈顶元素=value1存入局部变量表[0]=value1，替换旧值

(7)从[局部变量1]中装载值压入[操作数栈]

> 将局部变量表[1]=value压入栈顶

(8)返回栈顶元素

> 返回栈顶元素=value

分析至此，结论已经很明确了，返回的值时value。



# 4、结论

1、finally语句块是在return之前执行的。

2、return返回的值是 finally语句块 执行前的旧值。

分析此问题的主要目的是将理论结合实践来验证自己对字节码的理解。
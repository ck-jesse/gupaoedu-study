package com.coy.gupaoedu.study.data.structure.algorithm;

/**
 * 斐波那契数列
 *
 * @author chenck
 * @date 2019/7/25 14:13
 */
public class Fibonacci {


    /**
     * 递归方式计算斐波那契数
     * <p>
     * 缺点：递归方式重复计算太多
     */
    public static int fibonacciRecursive(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fibonacciRecursive(n - 1) + fibonacciRecursive(n - 2);
    }

    /**
     * 公式：F(n)=F(n-1)+F(n-2)
     * 特性：F(1)=1,F(2)=1
     * <p>
     * 求斐波那契数列第n项，结果对m求余
     * <p>
     * 循环
     */
    public static int fibonacci(int n, int m) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return 1;
        }
        // 需要开辟大块空间存储中间计算结果
        int value[] = new int[n + 1];
        value[1] = 1;// F(1)
        value[2] = 1;// F(2)
        // 后一项等于前两项的和
        for (int i = 3; i <= n; i++) {
            long result = (long) value[i - 1] + value[i - 2];
            value[i] = (int) (result > m ? result - m : result);
        }
        return value[n];
    }


    /**
     * 求斐波那契数列第n项，结果对m求余
     * <p>
     * 如果只让我们求一次斐波那契数列的第n项，或者n比较大，上面的方法就不好使了。
     * 观察发现我们只需要3个数的空间就够了，每次计算f(n)时f(n-3)就没用了，可以把存放f(n-3)的空间用来存放f(n)。使用循环数组可以实现，这里为了不计算mod3，把数组长度设置为了4。
     */
    public static int fibonacci2(int n, int m) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return 1;
        }
        // 循环队列用来存放计算结果,这里用4不用3为了计算方便
        int value[] = new int[4];
        value[1] = 1;// F(1)
        value[2] = 1;// F(2)
        // 后一项等于前两项的和
        for (int i = 3; i <= n; i++) {
            long result = (long) value[(i - 1) & 3] + value[(i - 2) & 3];
            value[i & 3] = (int) (result > m ? result - m : result);
        }
        return value[n & 3];
    }

}

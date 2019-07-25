package com.coy.gupaoedu.study.data.structure.algorithm;

import org.junit.Test;

import java.util.Scanner;

/**
 * 斐波那契数列
 *
 * @author chenck
 * @date 2019/7/25 14:13
 */
public class FibonacciTest {


    @Test
    public void fibonacciRecursive() {
        for (int n = 1; n < 30; n++) {
            System.out.println("斐波那契数 = " + Fibonacci.fibonacciRecursive(n));
        }
    }

    @Test
    public void fibonacci() {
        for (int n = 1; n < 30; n++) {
            System.out.println("斐波那契数 = " + Fibonacci.fibonacci(n, 19999997));
        }
    }

    @Test
    public void fibonacci2() {
        for (int n = 1; n < 30; n++) {
            System.out.println("斐波那契数 = " + Fibonacci.fibonacci2(n, 19999997));
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (true) {
            int n = in.nextInt();
            System.out.println("斐波那契数 = " + Fibonacci.fibonacci2(n, 19999997));
        }
    }

}

package com.coy.gupaoedu.study.data.structure;

/**
 * 利用堆数据结构解决TopN的问题
 *
 * 通过小顶堆的方式实现topN问题。大概逻辑就是先在数组的前n个中实现小顶堆（顶最小），后面每一个跟顶比较，如果小于等于顶，pass掉，负责替换顶，并调整最小堆。
 * 堆：是一个完全二叉树，一般用数组来表示一棵树。
 * 如果数组中节点的索引为n,则
 * 它的父节点的下标为: (n-1)/2
 * 它的左子节点的下标为: 2 * n + 1
 * 它的右子节点的下标为: 2 * n + 2
 *
 *
 * @author chenck
 * @date 2019/3/28 10:04
 */
public class TopN {

    // 父节点
    private int parent(int n) {
        return (n - 1) / 2;
    }

    // 左孩子
    private int left(int n) {
        return 2 * n + 1;
    }

    // 右孩子
    private int right(int n) {
        return 2 * n + 2;
    }

    // 构建堆
    // 将数组的前n个元素构建为堆
    private void buildHeap(int n, int[] data) {
        for (int i = 1; i < n; i++) {
            int t = i;
            // 调整堆
            // 第一轮：t=1,parent(t)=0，则data[0] > data[1]时，交换元素位置
            // 第二轮：t=2,parent(t)=1，则data[1] > data[2]时，交换元素位置
            while (t != 0 && data[parent(t)] > data[t]) {
                int temp = data[t];
                data[t] = data[parent(t)];
                data[parent(t)] = temp;
                t = parent(t);
            }
        }
    }

    // 调整data[i]
    private void adjust(int i, int n, int[] data) {
        if (data[i] <= data[0]) {
            return;
        }
        // 置换堆顶
        int temp = data[i];
        data[i] = data[0];
        data[0] = temp;
        // 调整堆顶
        int t = 0;
        while ((left(t) < n && data[t] > data[left(t)])
                || (right(t) < n && data[t] > data[right(t)])) {
            if (right(t) < n && data[right(t)] < data[left(t)]) {
                // 右孩子更小，置换右孩子
                temp = data[t];
                data[t] = data[right(t)];
                data[right(t)] = temp;
                t = right(t);
            } else {
                // 否则置换左孩子
                temp = data[t];
                data[t] = data[left(t)];
                data[left(t)] = temp;
                t = left(t);
            }
        }
    }

    // 寻找topN，该方法改变data，将topN排到最前面
    public void findTopN(int n, int[] data) {
        // 先构建n个数的小顶堆
        buildHeap(n, data);
        print(data);
        // n往后的数进行调整
        for (int i = n; i < data.length; i++) {
            adjust(i, n, data);
        }
    }

    // 打印数组
    public void print(int[] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + " ");
        }
        System.out.println();
    }
}

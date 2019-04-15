package com.coy.gupaoedu.study.data.structure.tree;

import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;

/**
 * @author chenck
 * @date 2019/4/12 10:38
 */
public class BinarySearchTreeTest {

    static BinarySearchTree<Integer, String> binaryTree = new BinarySearchTree<Integer, String>();

    @Before
    public void putTest() {
        // 节点插入tree中的先后顺序不同，节点的位置也不同
        binaryTree.put(5, "node1");
        binaryTree.put(6, "node6");
        binaryTree.put(4, "node4");
        binaryTree.put(2, "node2");
        binaryTree.put(3, "node3");
        binaryTree.put(8, "node8");
        binaryTree.put(7, "node7");
        binaryTree.put(10, "node10");
        // 替换原来的值
        binaryTree.put(5, "node555");
        System.out.println("初始BinaryTree： " + JSON.toJSONString(binaryTree));
    }

    @Test
    public void getTest() {
        System.out.println(binaryTree.get(5));
        System.out.println(binaryTree.get(3));
        System.out.println(binaryTree.get(4));
        System.out.println(binaryTree.get(5));
        System.out.println(binaryTree.get(6));
    }

    @Test
    public void selectTest() {
        // select一个不存在的
        System.out.println(JSON.toJSONString(binaryTree.select(0)));
        System.out.println(JSON.toJSONString(binaryTree.select(1)));
        System.out.println(JSON.toJSONString(binaryTree.select(2)));
        System.out.println(JSON.toJSONString(binaryTree.select(3)));
        System.out.println(JSON.toJSONString(binaryTree.select(4)));
        System.out.println(JSON.toJSONString(binaryTree.select(5)));
        System.out.println(JSON.toJSONString(binaryTree.select(6)));
        System.out.println(JSON.toJSONString(binaryTree.select(7)));
    }

    @Test
    public void rankTest() {
        System.out.println(binaryTree.rank(2));
        System.out.println(binaryTree.rank(3));
        System.out.println(binaryTree.rank(4));
        System.out.println(binaryTree.rank(5));
        System.out.println(binaryTree.rank(6));
        System.out.println(binaryTree.rank(7));
        System.out.println(binaryTree.rank(8));
        System.out.println(binaryTree.rank(10));
        // 不存在的元素
        System.out.println(binaryTree.rank(9));
    }

    @Test
    public void minTest() {
        System.out.println(JSON.toJSONString(binaryTree.min()));
        System.out.println(JSON.toJSONString(binaryTree.min()));
    }

    @Test
    public void maxTest() {
        System.out.println(JSON.toJSONString(binaryTree.max()));
        System.out.println(JSON.toJSONString(binaryTree.max()));
    }

    @Test
    public void deleteMinTest() {
        // 第一次删除
        binaryTree.deleteMin();
        System.out.println(JSON.toJSONString(binaryTree));
        // 第二次次删除
        binaryTree.deleteMin();
        System.out.println(JSON.toJSONString(binaryTree));
    }

    @Test
    public void deleteMaxTest() {
        // 第一次删除
        binaryTree.deleteMax();
        System.out.println(JSON.toJSONString(binaryTree));
        // 第二次次删除
        binaryTree.deleteMax();
        System.out.println(JSON.toJSONString(binaryTree));
    }

    @Test
    public void deleteTest() {
        binaryTree.delete(2);
        System.out.println(JSON.toJSONString(binaryTree));
        binaryTree.delete(4);
        System.out.println(JSON.toJSONString(binaryTree));
        binaryTree.delete(8);
        System.out.println(JSON.toJSONString(binaryTree));
    }

    @Test
    public void deleteRootTest() {
        binaryTree.delete(binaryTree.getRoot().getKey());
        System.out.println(JSON.toJSONString(binaryTree));
    }

}

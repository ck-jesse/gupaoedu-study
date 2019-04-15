package com.coy.gupaoedu.study.data.structure.tree;

/**
 * 红黑树：一种二叉查找树,但在每个节点增加一个存储位表示节点的颜色,可以是red或black.相对平衡二叉树(AVL树)来说,它的旋转次数变少.
 * <p>
 * 特性：
 * 1、每个节点非红即黑.
 * 2、根节点是黑的。
 * 3、每个叶节点(叶节点即树尾端NUL指针或NULL节点)都是黑的.
 * 4、如果一个节点是红的,那么它的两儿子都是黑的.
 * 5、对于任意节点而言,其到叶子点树NIL指针的每条路径都包含相同数目的黑节点.（每条路径都包含相同的黑节点）
 * <p>
 * 应用：
 * Map 和 Set的实现
 *
 * @author chenck
 * @date 2019/4/15 17:10
 */
public class RedBlackTree {
}

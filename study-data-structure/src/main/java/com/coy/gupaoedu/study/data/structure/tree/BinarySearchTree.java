package com.coy.gupaoedu.study.data.structure.tree;

import lombok.Data;

import java.util.NoSuchElementException;

/**
 * 二叉查找树也称为有序二叉查找树,Binary Search Tree
 * 特点：
 * 1、左子树的结点都要小于根结点，右子树的结点都要大于根结点。
 * 2、任意节点的左右子树也分别是二叉查找树
 * 3、没有键值相等的节点
 * 4、二叉树：即每个结点最多只有两个子结点的树
 * <p>
 * 注：在二叉查找树的基础上,又出现了平衡二叉树(AVL树),红黑树
 *
 * @author chenck
 * @date 2019/4/12 9:44
 */
// K 继承Comparable，以便对K进行排序
@Data
public class BinarySearchTree<K extends Comparable<K>, V> {

    /**
     * 根节点
     */
    private Node<K, V> root;

    /**
     * 返回节点数（含自己）
     * Returns the number of key-value pairs in this table
     */
    public int size() {
        return this.size(root);
    }

    private int size(Node<K, V> node) {
        if (null == node) {
            return 0;
        }
        return node.size;
    }

    /**
     * 判断是否为空或无子节点
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * 查找节点
     */
    public V get(K key) {
        if (null == key) {
            throw new IllegalArgumentException("first key to get() must be not null");
        }
        Node<K, V> node = this.get(root, key);
        if (null == node) {
            return null;
        }
        return node.value;
        //return this.getByLoop(root, key);
    }

    /**
     * 方式一：递归方式来查找节点
     */
    private Node<K, V> get(Node<K, V> node, K key) {
        if (null == key || null == node) {
            return null;
        }
        int compare = key.compareTo(node.key);
        // 如果key小于当前节点，就去找左边比当前小的节点
        if (compare < 0) {
            return get(node.leftNode, key);
        }
        // 如果key大于当前节点，就去找右边比当前大的节点
        if (compare > 0) {
            return get(node.rightNode, key);
        }
        return node;
    }

    /**
     * 方式二：循环方式来查找节点
     */
    private Node<K, V> getByLoop(Node<K, V> node, K key) {
        if (null == key || null == node) {
            return null;
        }
        Node<K, V> current = node;
        int compare = 0;
        while ((compare = key.compareTo(current.key)) != 0) {
            // 如果key小于当前节点，就去找左边比当前小的节点
            if (compare < 0) {
                current = current.leftNode;
            }
            // 如果key大于当前节点，就去找右边比当前大的节点
            if (compare > 0) {
                current = current.rightNode;
            }
            if (null == current) {
                return null;
            }
        }
        return current;
    }

    /**
     * 插入节点
     */
    public void put(K key, V value) {
        if (null == key) {
            throw new IllegalArgumentException("first key to put() must be not null");
        }
        root = this.put(root, key, value);
        //root = this.putByLoop(root, key, value);
    }

    /**
     * 递归方式来插入节点
     */
    private Node<K, V> put(Node<K, V> node, K key, V value) {
        if (null == node) {
            return new Node<K, V>(key, value);
        }
        int compare = key.compareTo(node.key);
        // 如果key小于当前节点，就去找左边比当前小的节点
        if (compare < 0) {
            node.leftNode = put(node.leftNode, key, value);
        }
        // 如果key大于当前节点，就去找右边比当前大的节点
        if (compare > 0) {
            node.rightNode = put(node.rightNode, key, value);
        }
        // 如果key等于当前节点
        if (compare == 0) {
            node.value = value;
        }
        // 重置节点数
        node.size = size(node.leftNode) + size(node.rightNode) + 1;
        return node;
    }

    /**
     * 循环方式来插入节点
     * 注：size存在不准确的缺陷，需要基于rootNode进行全部节点size的resize
     */
    private Node<K, V> putByLoop(Node<K, V> node, K key, V value) {
        if (null == node) {
            return new Node<K, V>(key, value);
        }

        Node<K, V> current = node;
        Node<K, V> parent = null;
        while (true) {
            parent = current;
            int compare = key.compareTo(current.key);
            // 如果key小于当前节点，就去找左边比当前小的节点
            if (compare < 0) {
                current = current.leftNode;
                if (null == current) {
                    parent.leftNode = new Node<K, V>(key, value);
                    // 重置节点数
                    // TODO 此处只能对parent的size进行重置，对于parent的parent的size无法重置，往上依次类推，current的所有parent的size均不准确
                    parent.size = size(parent.leftNode) + size(parent.rightNode) + 1;
                    break;
                }
            }
            // 如果key大于当前节点，就去找右边比当前大的节点
            if (compare > 0) {
                current = current.rightNode;
                if (null == current) {
                    parent.rightNode = new Node<K, V>(key, value);
                    // 重置节点数
                    parent.size = size(parent.leftNode) + size(parent.rightNode) + 1;
                    break;
                }
            }
            // 如果key等于当前节点
            if (compare == 0) {
                node.value = value;
                break;
            }
        }
        return node;
    }

    /**
     * select 根据排名从trees中查找对应的Node<k,v>
     *
     * @param ranking 排名 取值0到n-1，0表示最小值，n-1表示最大值
     */
    public Node<K, V> select(int ranking) {
        return this.select(root, ranking);
    }

    /**
     * select 根据排名从trees中查找对应的Node<k,v>
     * <p>
     * 二叉查找树的另外一个优点就是它可以一定程度上保证数据的有序性，所以可以较高效的去查询第n小的数据
     *
     * @param ranking 排名 取值0到n-1，0表示最小值，n-1表示最大值
     */
    public Node<K, V> select(Node<K, V> node, int ranking) {
        if (ranking < 0 || ranking >= size()) {
            throw new IllegalArgumentException("called select() with invalid argument: " + ranking);
        }
        if (node == null) {
            return null;
        }
        // 左子树中的结点都要小于根结点，所以只需计算左子树的大小，然后和排名比较
        // 左子树的大小等于排名时，则表示当前节点就是要找的节点
        int leftSize = size(node.leftNode);

        // 左子树的节点数大于排名时，则取左节点为基础来重新计算排名的节点
        if (leftSize > ranking) {
            return select(node.leftNode, ranking);
        }
        // 左子树的节点数小于排名时，则取右节点为基础来重新计算排名的节点
        if (leftSize < ranking) {
            // 为什么要重新计算ranking？
            // 因为以当前节点的rightNode作为新的初始节点来进行计算（将当前节点和左子树抛弃了），所以ranking需要减去leftSize再减1，以保持rightNode和ranking的同步。
            return select(node.rightNode, ranking - leftSize - 1);
        }
        return node;
    }

    /**
     * rank 根据指定key在tree中查找对应的排名
     */
    public int rank(K key) {
        return this.rank(root, key);
    }

    /**
     * rank 根据指定key在tree中查找对应的排名
     * <p>
     * 注：key必须是在tree内存在，否则key不存在计算的排名值是错误的
     */
    public int rank(Node<K, V> node, K key) {
        if (null == key) {
            throw new IllegalArgumentException("key is null");
        }
        if (node == null) {
            // 当node为空时，抛出异常，以免针对不存在的key计算出错误的排名
            throw new NoSuchElementException("Node is not exist of key：" + key);
            //return 0;
        }
        int compare = key.compareTo(node.key);
        if (compare < 0) {
            return rank(node.leftNode, key);
        }
        if (compare > 0) {
            // 如果key大于当前节点，就去右子树查找与key相等的节点并返回，同时将当前节点的左子树的节点数
            return 1 + size(node.leftNode) + rank(node.rightNode, key);
        }
        // key相等时，则排名等于左子树的节点数
        return size(node.leftNode);
    }

    /**
     * 最小节点
     */
    public Node<K, V> min() {
        return min(root);
    }

    /**
     * 最小节点
     */
    public Node<K, V> min(Node<K, V> node) {
        if (null == node) {
            throw new IllegalArgumentException("node is null");
        }
        // 最小节点 = 树中最左边的结点（即左子树中没有左节点的节点）
        if (null == node.leftNode) {
            return node;
        }
        return min(node.leftNode);
    }

    /**
     * 最大节点
     */
    public Node<K, V> max() {
        return max(root);
    }

    /**
     * 最大节点
     */
    public Node<K, V> max(Node<K, V> node) {
        if (null == node) {
            throw new IllegalArgumentException("node is null");
        }
        // 最大节点 = 树中最右边的结点（即右子树中没有右节点的节点）
        if (null == node.rightNode) {
            return node;
        }
        return max(node.rightNode);
    }

    /**
     * 删除最小节点
     */
    public void deleteMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Symbol table underflow");
        }
        root = deleteMin(root);
    }

    /**
     * 删除最小节点
     */
    public Node<K, V> deleteMin(Node<K, V> node) {
        if (null == node) {
            return null;
        }
        // 最小节点 = 树中最左边的结点（即左子树中没有左节点的节点）
        if (null == node.leftNode) {
            // 返回右节点B
            return node.rightNode;
        }
        // 将右节点B替换需删除的左节点
        node.leftNode = deleteMin(node.leftNode);
        node.size = size(node.leftNode) + size(node.rightNode) + 1;
        return node;
    }

    /**
     * 删除最大节点
     */
    public void deleteMax() {
        if (isEmpty()) {
            throw new NoSuchElementException("Symbol table underflow");
        }
        root = deleteMax(root);
    }

    /**
     * 删除最大节点
     */
    public Node<K, V> deleteMax(Node<K, V> node) {
        if (null == node) {
            return null;
        }
        // 最大节点 = 树中最右边的结点（即右子树中没有右节点的节点）
        if (null == node.rightNode) {
            // 返回左节点A
            return node.leftNode;
        }
        // 将左节点A替换需删除的左节点
        node.rightNode = deleteMax(node.rightNode);
        node.size = size(node.leftNode) + size(node.rightNode) + 1;
        return node;
    }

    /**
     * 删除节点
     */
    public void delete(K key) {
        if (null == key) {
            throw new IllegalArgumentException("key is null");
        }
        root = delete(root, key);
    }

    /**
     * 删除节点
     * 逻辑：
     * 1、找到需要删除的结点E
     * 2、在E的右子树中找到最小结点H
     * 3、用H替代E即可
     */
    public Node<K, V> delete(Node<K, V> node, K key) {
        if (null == node) {
            return null;
        }
        int compare = key.compareTo(node.key);
        if (compare < 0) {
            // key小于当前节点，则从左子树中查找对应节点
            node.leftNode = delete(node.leftNode, key);
        } else if (compare > 0) {
            // key大于当前节点，则从右子树中查找对应节点
            node.rightNode = delete(node.rightNode, key);
        } else {
            // key等于当前节点
            // 右节点为空，直接返回左节点来替代删除结点
            if (node.rightNode == null) {
                return node.leftNode;
            }
            // 左节点为空，直接返回右节点来替代删除结点
            if (node.leftNode == null) {
                return node.rightNode;
            }
            Node tempNode = node;
            // 左右节点都不为空，则查找右子树中最小的节点，并替换删除节点（即当前节点）
            node = min(tempNode.rightNode);

            // 删除右子树中最小的节点
            node.rightNode = deleteMin(tempNode.rightNode);

            // 用删除节点的左节点替换最小节点的左节点
            node.leftNode = tempNode.leftNode;
        }
        // update links and node count after recursive calls
        node.size = size(node.leftNode) + size(node.rightNode) + 1;
        return node;
    }


    /**
     * 键值表Node
     *
     * @author chenck
     * @date 2019/4/12 10:04
     */
    @Data
    public static class Node<K, V> {

        public Node(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }

        public Node(K key, V value) {
            this(key, value, 1);
        }

        /**
         * 数据key
         */
        private K key;
        /**
         * 数据项
         */
        private V value;
        /**
         * 节点数(含自己)
         */
        private int size;
        /**
         * 左子节点（左子树）
         */
        private Node<K, V> leftNode;
        /**
         * 右子节点（右子树）
         */
        private Node<K, V> rightNode;
    }

}

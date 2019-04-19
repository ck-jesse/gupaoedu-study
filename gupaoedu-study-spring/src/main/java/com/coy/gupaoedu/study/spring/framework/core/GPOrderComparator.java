package com.coy.gupaoedu.study.spring.framework.core;

import java.util.Comparator;

/**
 * 对实现了Ordered接口的对象进行排序时使用
 *
 * @author chenck
 * @date 2019/4/19 15:50
 */
public class GPOrderComparator implements Comparator<Object> {
    /**
     * Shared default instance of {@code OrderComparator}.
     */
    public static final GPOrderComparator INSTANCE = new GPOrderComparator();

    @Override
    public int compare(Object o1, Object o2) {
        // 优先对象处理
        boolean p1 = (o1 instanceof GPPriorityOrdered);
        boolean p2 = (o2 instanceof GPPriorityOrdered);
        if (p1 && !p2) {
            return -1;
        } else if (p2 && !p1) {
            return 1;
        }

        // Direct evaluation instead of Integer.compareTo to avoid unnecessary object creation.
        int i1 = getOrder(o1);
        int i2 = getOrder(o2);
        return (i1 < i2) ? -1 : (i1 > i2) ? 1 : 0;
    }

    protected int getOrder(Object obj) {
        if (obj != null) {
            Integer order = findOrder(obj);
            if (order != null) {
                return order;
            }
        }
        return GPOrdered.LOWEST_PRECEDENCE;
    }

    protected Integer findOrder(Object obj) {
        return (obj instanceof GPOrdered ? ((GPOrdered) obj).getOrder() : null);
    }
}

package com.coy.gupaoedu.study.pattern.chainofresponsibility.aop;

import java.util.ArrayList;
import java.util.List;

/**
 * 对过滤链的抽象（横切关注点），是多个过滤器的聚集，本质上，FilterChain 也可以看作是一个大的Filter
 *
 * @author chenck
 * @date 2019/3/21 20:01
 */
public class FilterChain {
    List<Filter> filters = new ArrayList<Filter>();
    int index = 0;

    /**
     * 链式编程
     *
     * @param
     * @author chenck
     * @date 2019/3/21 20:04
     */
    public FilterChain addFilter(Filter filter) {
        filters.add(filter);
        // 返回自身
        return this;
    }

    public void doFilter(Request request, Response response) {
        if (index == filters.size()) {
            return;
        }
        Filter filter = filters.get(index);
        index++;
        filter.doFilter(request, response, this);
    }
}

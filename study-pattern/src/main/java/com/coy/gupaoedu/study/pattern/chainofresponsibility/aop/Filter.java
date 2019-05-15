package com.coy.gupaoedu.study.pattern.chainofresponsibility.aop;

/**
 * 抽象处理者
 *
 * @author chenck
 * @date 2019/3/21 20:01
 */
public interface Filter {

    /**
     * 每个Filter均为FilterChain的成员, Filter持有FilterChain的引用，以便调用链条中的各处理者
     *
     * @param
     * @author chenck
     * @date 2019/3/21 20:02
     */
    public void doFilter(Request request, Response response, FilterChain chain);

}

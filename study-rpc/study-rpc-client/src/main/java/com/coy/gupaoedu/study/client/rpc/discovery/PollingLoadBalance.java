package com.coy.gupaoedu.study.client.rpc.discovery;

import com.coy.gupaoedu.study.server.rpc.RpcUrl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡
 *
 * @author chenck
 * @date 2020/6/17 10:33
 */
public class PollingLoadBalance extends AbstractLoadBalance {

    private static final Integer MAX = Integer.MAX_VALUE - 1000000;

    AtomicInteger counter = new AtomicInteger(0);

    @Override
    public RpcUrl select(List<RpcUrl> serviceList) {
        counter.compareAndSet(MAX, 0);// 超限重置为0
        return serviceList.get(counter.incrementAndGet() % serviceList.size());
    }
}

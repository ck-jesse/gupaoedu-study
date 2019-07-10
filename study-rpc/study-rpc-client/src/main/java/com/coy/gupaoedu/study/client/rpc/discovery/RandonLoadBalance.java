package com.coy.gupaoedu.study.client.rpc.discovery;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡
 *
 * @author chenck
 * @date 2019/7/10 22:42
 */
public class RandonLoadBalance extends AbstractLoadBalance {

    @Override
    public String select(List<String> serviceList) {
        // 生成一个随机数务
        Random random = new Random(serviceList.size());
        return serviceList.get(random.nextInt());
    }
}

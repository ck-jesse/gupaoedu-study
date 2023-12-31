package com.coy.gupaoedu.study.client.rpc.discovery;

import com.coy.gupaoedu.study.server.rpc.RpcUrl;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author chenck
 * @date 2019/7/10 22:44
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public RpcUrl selectService(List<RpcUrl> serviceList) {
        if (CollectionUtils.isEmpty(serviceList)) {
            return null;
        }
        if (serviceList.size() == 1) {
            return serviceList.get(0);
        }
        return select(serviceList);
    }

    public abstract RpcUrl select(List<RpcUrl> serviceList);
}

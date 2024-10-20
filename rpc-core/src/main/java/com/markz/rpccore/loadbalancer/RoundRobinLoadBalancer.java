package com.markz.rpccore.loadbalancer;

import com.markz.rpccore.model.ServiceProviderMeta;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器
 */
public class RoundRobinLoadBalancer implements LoadBalance {

    /**
     * 轮询游标
     */
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    public ServiceProviderMeta selectServer(List<ServiceProviderMeta> serviceMetaList) {
        if (serviceMetaList.isEmpty()) {
            return null;
        }
        int size = serviceMetaList.size();
        // 只有一个服务，无需轮询
        if (size == 1) {
            return serviceMetaList.get(0);
        }
        int index = currentIndex.getAndIncrement() % size;
        return serviceMetaList.get(index);
    }
}

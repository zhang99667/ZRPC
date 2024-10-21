package com.markz.rpccore.loadbalancer;

import com.markz.rpccore.model.ServiceProviderMeta;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡器
 */
public class RandomLoadBalancer implements LoadBalance {
    private final Random random = new Random();

    @Override
    public ServiceProviderMeta selectServer(List<ServiceProviderMeta> serviceMetaList) {
        int size = serviceMetaList.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return serviceMetaList.get(0);
        }
        return serviceMetaList.get(random.nextInt(size));
    }
}

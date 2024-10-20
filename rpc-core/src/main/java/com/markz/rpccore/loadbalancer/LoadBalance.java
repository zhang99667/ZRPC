package com.markz.rpccore.loadbalancer;

import com.markz.rpccore.model.ServiceProviderMeta;
import java.util.List;

/**
 * 负载均衡
 */
public interface LoadBalance {

    /**
     * 选择可用服务
     *
     * @param serviceMetaList 可用服务列表
     * @return
     */
    ServiceProviderMeta selectServer(List<ServiceProviderMeta> serviceMetaList);
}

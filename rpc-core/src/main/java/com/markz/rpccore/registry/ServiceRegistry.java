package com.markz.rpccore.registry;

import com.markz.rpccore.model.ServiceProviderMeta;

import java.util.List;

public interface ServiceRegistry {
    /**
     * 初始化客户端
     */
    void init();

    /**
     * 服务注册
     *
     * @param serviceProviderMeta 服务提供者元信息
     */
    void registerService(ServiceProviderMeta serviceProviderMeta);

    /**
     * 服务注销
     *
     * @param serviceProviderMeta 服务提供者元信息
     */
    void unRegisterService(ServiceProviderMeta serviceProviderMeta);

    /**
     * 服务发现
     *
     * @param serviceKey 服务
     * @return
     */
    List<ServiceProviderMeta> serviceDiscovery(String serviceKey);

    /**
     * 关闭客户端
     */
    void shutdown();
}

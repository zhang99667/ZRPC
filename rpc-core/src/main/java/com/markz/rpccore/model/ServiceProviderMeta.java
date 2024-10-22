package com.markz.rpccore.model;

import lombok.Data;

/**
 * 服务提供者元信息
 * key: /业务前缀/服务名/服务节点地址
 */
@Data
public class ServiceProviderMeta {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 主机名
     */
    private String serviceHost;

    /**
     * 端口号
     */
    private String servicePort;

    /**
     * 服务提供者权重，用于 loadbalancer
     */
    private Integer weight;

    /**
     * 返回 key
     * 格式：类全限定名/主机:端口
     * <p>
     * com.markz.Service.UserService/localhost:8081
     *
     * @return String: com.markz.Service.UserService/localhost:8081
     */
    public String getServiceKey() {
        return String.format("%s/%s:%s", serviceName, serviceHost, servicePort);
    }
}

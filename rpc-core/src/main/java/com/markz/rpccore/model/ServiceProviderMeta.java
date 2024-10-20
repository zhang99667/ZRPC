package com.markz.rpccore.model;

import lombok.Data;

/**
 * 服务提供者元信息
 */
@Data
public class ServiceProviderMeta {

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
}

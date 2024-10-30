package com.markz.rpccore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * RPC 核心配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rpc")
public class RpcConfiguration {
    /**
     * 服务名称
     */
    private String name = "ZRPC";

    /**
     * 版本信息
     */
    private String version = "1.0";

    /**
     * 主机名
     */
    private String serverHost = "localhost";

    /**
     * 端口号
     */
    private Integer serverPort = 8090;

    /**
     * 序列化器类型
     */
    private String serializerType;

    /**
     * 负载均衡器类型
     */
    private String loadbalancerType;

    /**
     * 指示当前是 client 还是 server
     */
    private String cors;

    /**
     * 是否启用 mock 模式
     */
    private boolean mock;
}

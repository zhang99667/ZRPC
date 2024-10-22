package com.markz.rpccore.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 注册中心配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rpc.registry")
public class RegistryConfiguration {
    /**
     * 注册中心类型
     */
    private String type;

    /**
     * 注册中心地址
     */
    private String host;

    /**
     * 注册中心端口号
     */
    private Integer port;

    /**
     * 注册中心用户名
     */
    private String username;

    /**
     * 注册中心密码
     */
    private String password;

    /**
     * 超时时间（毫秒）
     */
    private int timeout = 10000;

    private String zkRoot = "/rpc";

    /**
     * 拼接 Zookeeper 地址
     * 如果有用户名和密码，返回带用户名和密码的 zkAddress
     *
     * @return zkAddress
     */
    public String getZkAddress() {
        if (username != null && password != null) {
            // 如果用户名和密码都不为空，返回带认证信息的地址
            return username + ":" + password + "@" + host + ":" + port;
        }
        // 否则返回不带认证信息的地址
        return host + ":" + port;
    }
}

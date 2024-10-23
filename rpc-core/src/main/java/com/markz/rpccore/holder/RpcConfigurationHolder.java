package com.markz.rpccore.holder;

import com.markz.rpccore.config.RegistryConfiguration;
import com.markz.rpccore.config.RpcConfiguration;
import lombok.Getter;

/**
 * RpcConfigurationHolder
 */
public class RpcConfigurationHolder {

    /**
     * -- GETTER --
     * 获取 RegistryConfig
     */
    @Getter
    private static RegistryConfiguration registryConfig;

    /**
     * -- GETTER --
     * 获取 rpcConfig
     */
    @Getter
    private static RpcConfiguration rpcConfig;

    /**
     * 设置 RegistryConfiguration
     *
     * @param registryConfiguration RegistryConfiguration
     */
    public static void addRegistryConfiguration(RegistryConfiguration registryConfiguration) {
        registryConfig = registryConfiguration;
    }

    /**
     * 设置 rpcConfiguration
     *
     * @param rpcConfiguration rpcConfiguration
     */
    public static void addRpcConfiguration(RpcConfiguration rpcConfiguration) {
        rpcConfig = rpcConfiguration;
    }

}

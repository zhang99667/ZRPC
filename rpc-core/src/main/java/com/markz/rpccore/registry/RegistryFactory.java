package com.markz.rpccore.registry;

import com.markz.rpccore.registry.zookeeper.ZookeeperClient;
import com.markz.rpccore.util.SpiLoader;

/**
 * 注册中心工厂
 */
public class RegistryFactory {

    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new ZookeeperClient();

    /**
     * 获取注册中心实例
     *
     * @param key key
     * @return instance
     */
    public static Registry getRegistry(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }
}

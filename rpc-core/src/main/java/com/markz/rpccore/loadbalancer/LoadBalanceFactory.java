package com.markz.rpccore.loadbalancer;

import com.markz.rpccore.util.SpiLoader;

/**
 * 负载均衡器工厂
 */
public class LoadBalanceFactory {

    static {
        SpiLoader.load(LoadBalance.class);
    }

    /**
     * 默认负载均衡器
     */
    private static final LoadBalance DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    /**
     * 获取负载均衡器实例
     *
     * @param key key
     * @return instance
     */
    public static LoadBalance getLoadBalancer(String key) {
        return SpiLoader.getInstance(LoadBalance.class, key);
    }
}

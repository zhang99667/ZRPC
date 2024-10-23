package com.markz.rpccore.bootstrap.client;

import com.markz.rpccore.config.RegistryConfiguration;
import com.markz.rpccore.config.RpcConfiguration;
import com.markz.rpccore.holder.RpcConfigurationHolder;
import com.markz.rpccore.registry.Registry;
import com.markz.rpccore.registry.RegistryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 客户端启动类
 */
@Slf4j
@Component
public class RpcClientBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private RegistryConfiguration registryConfiguration;

    @Resource
    private RpcConfiguration rpcConfiguration;

    /**
     * rpc client 入口
     * 1. 保存配置文件
     * 2. 连接注册中心
     */
    public void run() {
        // 1. 保存配置文件
        RpcConfigurationHolder.addRegistryConfiguration(registryConfiguration);
        RpcConfigurationHolder.addRpcConfiguration(rpcConfiguration);

        // 2. 连接注册中心
        String registryType = registryConfiguration.getType();
        Registry registry = RegistryFactory.getRegistry(registryType);
        registry.init();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 客户端的启动方式
        if ("client".equals(rpcConfiguration.getCors())) {
            run();
        }
    }
}

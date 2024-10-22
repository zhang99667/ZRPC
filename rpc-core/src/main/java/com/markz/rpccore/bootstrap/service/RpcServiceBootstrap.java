package com.markz.rpccore.bootstrap.service;

import com.markz.rpccore.config.RegistryConfiguration;
import com.markz.rpccore.config.RpcConfiguration;
import com.markz.rpccore.registry.Registry;
import com.markz.rpccore.registry.RegistryFactory;
import com.markz.rpccore.spring.RegisterRpcServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 服务端启动类
 */
@Slf4j
@Component
public class RpcServiceBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * rpc service 入口
     * 1. 服务注册
     * 2. 监听端口
     */
    @Resource
    private NettyRpcService rpcService;

    @Resource
    private RegisterRpcServices registerRpcServices;

    @Resource
    private RpcConfiguration rpcConfiguration;

    @Resource
    private RegistryConfiguration registryConfiguration;

    public void run() throws InterruptedException {
        // 1. 连接注册中心
        String registry = registryConfiguration.getType();
        Registry zookeeper = RegistryFactory.getRegistry(registry);
        zookeeper.init();

        // 2. 注册服务
        registerRpcServices.registerServices();
        // 3. start server by netty
        rpcService.startServer(rpcConfiguration.getServerPort());
    }

    /**
     * 通过监听器来运行
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 服务端的启动方式
        try {
            if ("server".equals(rpcConfiguration.getCors())) {
                run();
            }
        } catch (InterruptedException e) {
            log.info("启动失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

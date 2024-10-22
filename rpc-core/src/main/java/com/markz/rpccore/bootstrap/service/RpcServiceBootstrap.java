package com.markz.rpccore.bootstrap.service;

import com.markz.rpccore.config.RpcConfiguration;
import com.markz.rpccore.registry.zookeeper.ZookeeperClient;
import com.markz.rpccore.spring.RegisterRpcServices;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class RpcServiceBootstrap {
    /**
     * rpc service 入口
     * 1. 服务注册
     * 2. 监听端口
     */
    @Resource
    private NettyRpcService rpcService;

    // @Resource
    // private ZookeeperClient zkClient;

    @Resource
    private RegisterRpcServices registerRpcServices;

    @Resource
    private RpcConfiguration rpcConfiguration;

    public void run() throws InterruptedException {
        // // 1. 连接注册中心
        // zkClient.init();
        // // 2. 注册服务
        registerRpcServices.registerServices();
        // 3. start server by netty
        rpcService.startServer(rpcConfiguration.getServerPort());
    }

}

package com.markz.rpccore.bootstrap.service;

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

    public void run() throws InterruptedException {
        // TODO 注册中心
        // 2. start server by netty
        rpcService.startServer(8090);
    }

}

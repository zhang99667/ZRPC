package com.markz.rpccore.bootstrap.service;

/**
 * Server 接口，方便对后续其他 NIO 框架进行扩展
 */
public interface RpcServer {

    void startServer(int port) throws InterruptedException;
}

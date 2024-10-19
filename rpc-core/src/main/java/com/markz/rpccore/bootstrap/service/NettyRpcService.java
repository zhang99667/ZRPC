package com.markz.rpccore.bootstrap.service;

import com.markz.rpccore.bootstrap.service.initializer.RpcServiceInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 基于 Netty 的 rpc 服务端
 */
@Slf4j
@Component
public class NettyRpcService implements RpcServer {

    @Override
    public void startServer(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap(); // (2)
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new RpcServiceInitializer())
                    // 表示在三次握手阶段，服务器最大允许多少个客户端连接请求在队列中等待处理
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    // TCP 的心跳机制
                    .childOption(ChannelOption.SO_KEEPALIVE, false); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture channelFuture = bootstrap.bind(8090).sync(); // (7)
            log.info("Netty 服务器启动...");
            log.info("正在监听端口{}...", port);
            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
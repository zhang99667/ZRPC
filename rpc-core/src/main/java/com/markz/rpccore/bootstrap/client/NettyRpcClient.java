package com.markz.rpccore.bootstrap.client;

import com.markz.common.entity.rpc.RpcRequest;
import com.markz.common.entity.rpc.RpcResponse;
import com.markz.rpccore.bootstrap.client.initializer.RpcClientInitializer;
import com.markz.rpccore.holder.RpcRequestHolder;
import com.markz.rpccore.request.RequestPromise;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class NettyRpcClient {

    public RpcResponse connectionAndSend(String serverIp, int serverPort, RpcRequest rpcRequest) {
        // 如果 Channel 不存在就建立一个 Channel。
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    // 客户端不是主从 Reactor 没有 childHandler 的说法
                    .handler(new RpcClientInitializer());
            // 1. 建立连接
            ChannelFuture f = bootstrap.connect(serverIp, serverPort).sync();
            if (f.isSuccess()) {
                // 2. 保存 Channel，每个实例创建好 Channel 后面复用
                Channel channel = f.channel();
                RpcRequestHolder.addChannelMapping(serverIp, serverPort, channel);
                // 3. 发送数据
                /*
                 这里是 nio 线程异步执行的，所以通过 Promise 阻塞然后获取
                 但是获取异步回调还有问题，请求和响应需要一一对应。不然在高并
                 发的场景下，会发生请求和响应不一致的问题。
                */
                // channel = RpcRequestHolder.getChannel(serverIp, serverPort);
                RequestPromise requestPromise = new RequestPromise(channel.eventLoop());
                RpcRequestHolder.addRequestPromise(rpcRequest.getRequestId(), requestPromise);
                channel.writeAndFlush(rpcRequest).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("RpcRequest 发送成功: {}", rpcRequest);
                    } else {
                        log.error("RpcRequest 发送失败: {}", future.cause().getMessage());
                        throw new RuntimeException("RpcRequest 发送失败: {}" + future.cause().getMessage());
                    }
                });

                // 4. 获取异步结果
                try {
                    RpcResponse rpcResponse = requestPromise.get();
                    log.info("收到响应:{}", rpcResponse);
                    return rpcResponse;
                } catch (InterruptedException | ExecutionException e) {
                    log.info("获取异步结果错误,{}", e.getMessage());
                    throw new RuntimeException(e);
                } finally {
                    // 移除 Promise 防止 OOM
                    RpcRequestHolder.removeRequestPromise(rpcRequest.getRequestId());
                }

            }
        } catch (InterruptedException e) {
            log.error("连接错误,{}", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }
        return new RpcResponse();
    }
}
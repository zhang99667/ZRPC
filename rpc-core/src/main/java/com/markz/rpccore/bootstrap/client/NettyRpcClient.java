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
                // Channel channel = f.channel();
                // RpcRequestHolder.addChannelMapping(serverIp, serverPort, channel);

                /*
                 这里是 nio 线程异步执行的，所以通过 Promise 阻塞然后获取
                 但是获取异步回调还有问题，请求和响应需要一一对应。不然在高并
                 发的场景下，会发生请求和响应不一致的问题。
                */
                // channel = RpcRequestHolder.getChannel(serverIp, serverPort);

                // 3. 保存 Promise
                RequestPromise requestPromise = new RequestPromise(f.channel().eventLoop());
                RpcRequestHolder.addRequestPromise(rpcRequest.getRequestId(), requestPromise);

                // 4. 发送数据
                f.channel().writeAndFlush(rpcRequest).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("RpcRequest 发送成功: {}", rpcRequest);
                    } else {
                        log.error("RpcRequest 发送失败: {}", future.cause().getMessage());
                        throw new RuntimeException("RpcRequest 发送失败: {}" + future.cause().getMessage());
                    }
                });

                // 5. 获取异步结果
                try {
                    RpcResponse rpcResponse = requestPromise.get();
                    if (rpcResponse == null) {
                        throw new RuntimeException("远程调用异常");
                    }
                    log.info("收到响应:{}", rpcResponse);
                    return rpcResponse;
                } catch (InterruptedException | ExecutionException e) {
                    log.info("获取异步结果错误,{}", e.getMessage());
                    throw new RuntimeException(e);
                } finally {
                    // 6. 移除 Promise 防止 OOM
                    RpcRequestHolder.removeRequestPromise(rpcRequest.getRequestId());
                    // 7. 关闭连接
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
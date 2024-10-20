package com.markz.rpccore.handler;

import com.markz.common.entity.rpc.RpcResponse;
import com.markz.rpccore.holder.RpcRequestHolder;
import com.markz.rpccore.request.RequestPromise;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 接受 Response
 */
@Slf4j
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcResponse> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接就绪");
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) {
        log.info("收到响应：{}", rpcResponse);
        RequestPromise requestPromise = RpcRequestHolder.getRequestPromise(rpcResponse.getRequestId());
        if (requestPromise != null) {
            // setSuccess 结束阻塞
            requestPromise.setSuccess(rpcResponse);
        } else {
            log.error("没有匹配到 RequestPromise，requestId: {}", rpcResponse.getRequestId());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("通道异常：{}", cause.getMessage());
        ctx.close();
    }
}

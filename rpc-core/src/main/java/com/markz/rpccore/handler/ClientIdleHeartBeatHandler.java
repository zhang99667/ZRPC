package com.markz.rpccore.handler;

import cn.hutool.core.util.IdUtil;
import com.markz.rpccore.constant.HeartBeatConstant;
import com.markz.rpccore.protocol.HeartBeatMessage;
import com.markz.rpccore.protocol.ProtocolHeader;
import com.markz.rpccore.protocol.ProtocolMessageTypeEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 客户端心跳保活
 */
@Slf4j
public class ClientIdleHeartBeatHandler extends IdleStateHandler {

    public ClientIdleHeartBeatHandler() {
        super(0,
                HeartBeatConstant.WRITER_IDLE_TIME_SECONDS,
                HeartBeatConstant.ALL_IDLE_TIME_SECONDS,
                TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        // 1. 创建心跳机制消息
        HeartBeatMessage<Byte> heartBeatMessage = new HeartBeatMessage<Byte>();
        ProtocolHeader protocolHeader = ProtocolHeader.builder()
                .requestId(IdUtil.getSnowflakeNextId())
                .type((byte) ProtocolMessageTypeEnum.HEART_BEAT.ordinal())
                .build();
        heartBeatMessage.setProtocolHeader(protocolHeader);
        heartBeatMessage.setBody(null);

        // 2. 写空闲，发起心跳请求
        if (evt.state() == IdleState.WRITER_IDLE) {
            ctx.writeAndFlush(heartBeatMessage).addListener(future -> {
                if (future.isSuccess()) {
                    log.info("心跳发送成功...");
                } else {
                    log.error("心跳发送失败...");
                    throw new RuntimeException("heartBeatMessage 发送失败: {}" + future.cause().getMessage());
                }
            });
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("心跳发送异常");
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered");
        ctx.close();       // 关闭连接
        super.channelUnregistered(ctx);
    }
}
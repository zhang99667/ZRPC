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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 客户端心跳保活
 */
@Slf4j
public class ClientIdleHeartBeatHandler extends IdleStateHandler {

    public ClientIdleHeartBeatHandler() {
        super(HeartBeatConstant.READER_IDLE_TIME_SECONDS,
                HeartBeatConstant.WRITER_IDLE_TIME_SECONDS,
                HeartBeatConstant.ALL_IDLE_TIME_SECONDS,
                TimeUnit.SECONDS);
    }

    /**
     * 空闲计数器
     */
    public static final AtomicInteger idleCount = new AtomicInteger(0);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            // 1. 创建心跳机制消息
            HeartBeatMessage<Byte> heartBeatMessage = new HeartBeatMessage<Byte>();
            ProtocolHeader protocolHeader = ProtocolHeader.builder()
                    .requestId(Long.parseLong(IdUtil.fastUUID()))
                    .type((byte) ProtocolMessageTypeEnum.HEART_BEAT.ordinal())
                    .build();
            heartBeatMessage.setProtocolHeader(protocolHeader);
            heartBeatMessage.setBody(null);
            // 2. 读空闲或写空闲，发起心跳请求
            if (event.state() == IdleState.READER_IDLE || event.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(heartBeatMessage).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("心跳发送成功...");
                        // 3. 实例无响应，关闭连接
                        if (idleCount.incrementAndGet() == HeartBeatConstant.MAX_IDLE_COUNT) {
                            log.error("实例无响应，正在关闭连接...");
                            ctx.close();
                        }
                    } else {
                        log.error("心跳发送失败...");
                        throw new RuntimeException("heartBeatMessage 发送失败: {}" + future.cause().getMessage());
                    }
                });
            }
        } else {
            super.userEventTriggered(ctx, evt);
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
        idleCount.set(0);  // 重置空闲计数器
        ctx.close();       // 关闭连接
        super.channelUnregistered(ctx);
    }

    /**
     * 收到服务端响应，重置计数器
     *
     * @param ctx ChannelHandlerContext
     * @param msg HeartBeatMessage
     * @throws Exception Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HeartBeatMessage) {
            idleCount.set(0);
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
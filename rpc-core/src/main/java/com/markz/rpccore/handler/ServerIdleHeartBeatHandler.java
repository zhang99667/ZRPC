package com.markz.rpccore.handler;

import cn.hutool.core.util.IdUtil;
import com.markz.rpccore.protocol.HeartBeatMessage;
import com.markz.rpccore.protocol.ProtocolHeader;
import com.markz.rpccore.protocol.ProtocolMessage;
import com.markz.rpccore.protocol.ProtocolMessageTypeEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端心跳保活
 */
@Slf4j
public class ServerIdleHeartBeatHandler extends SimpleChannelInboundHandler<HeartBeatMessage<Byte>> {

    // public ServerHeartBeatHandler() {
    //     super(HeartBeatConstant.READER_IDLE_TIME_SECONDS,
    //             HeartBeatConstant.WRITER_IDLE_TIME_SECONDS,
    //             HeartBeatConstant.ALL_IDLE_TIME_SECONDS,
    //             TimeUnit.SECONDS);
    // }

    /**
     * 收到客户端心跳消息，给出相应
     *
     * @param ctx ChannelHandlerContext
     * @param msg HeartBeatMessage
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatMessage msg) {
        if (msg != null) {
            ProtocolMessage<?> message = (ProtocolMessage<?>) msg;
            if (message.getProtocolHeader().getType() == ProtocolMessageTypeEnum.HEART_BEAT.ordinal()) {
                // 处理心跳消息
                log.info("收到心跳消息");
                HeartBeatMessage<Byte> heartBeatMessage = new HeartBeatMessage<Byte>();
                ProtocolHeader header = ProtocolHeader.builder()
                        .requestId(Long.parseLong(IdUtil.fastUUID()))
                        .type((byte) ProtocolMessageTypeEnum.HEART_BEAT.ordinal())
                        .build();
                heartBeatMessage.setProtocolHeader(header);
                heartBeatMessage.setBody(null);
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
    }
}
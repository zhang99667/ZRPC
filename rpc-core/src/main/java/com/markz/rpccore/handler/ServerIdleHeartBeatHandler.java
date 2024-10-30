package com.markz.rpccore.handler;

import com.markz.rpccore.constant.HeartBeatConstant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 服务端心跳保活
 */
@Slf4j
public class ServerIdleHeartBeatHandler extends IdleStateHandler {

    public ServerIdleHeartBeatHandler() {
        super(HeartBeatConstant.READER_IDLE_TIME_SECONDS,
                0,
                HeartBeatConstant.ALL_IDLE_TIME_SECONDS,
                TimeUnit.SECONDS);
    }

    /**
     * 收到客户端心跳消息，给出相应
     *
     * @param ctx ChannelHandlerContext
     * @param evt Object
     * @throws Exception Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            // 读空闲事件
            if (event.state() == IdleState.READER_IDLE) {
                log.debug("长时间没有收到消息了，断开连接");
                ctx.close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
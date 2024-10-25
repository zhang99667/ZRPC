package com.markz.rpccore.bootstrap.client.initializer;

import com.markz.rpccore.codec.body.RpcBodyDecoder;
import com.markz.rpccore.codec.body.RpcBodyEncoder;
import com.markz.rpccore.codec.frame.FrameDecoder;
import com.markz.rpccore.codec.protocol.ProtocolDecoder;
import com.markz.rpccore.codec.protocol.ProtocolEncoder;
import com.markz.rpccore.handler.RpcResponseHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * client pipeline
 */
public class RpcClientInitializer extends ChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // outbound handler
        ch.pipeline().addLast("ProtocolEncoder", new ProtocolEncoder()); // 封装成帧
        ch.pipeline().addLast("Serializer", new RpcBodyEncoder()); // 序列化

        // inbound handler
        ch.pipeline().addLast("FrameDecoder", new FrameDecoder()); // 定长解码器
        ch.pipeline().addLast("ProtocolDecoder", new ProtocolDecoder()); // 信息校验
        ch.pipeline().addLast("Deserializer", new RpcBodyDecoder()); // 反序列化
        ch.pipeline().addLast("RpcResponseHandler", new RpcResponseHandler()); // 处理 RpcResponse
    }
}

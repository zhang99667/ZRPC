package com.markz.rpccore.bootstrap.client.initializer;

import com.markz.rpccore.codec.fastjson.FastJsonDecoder;
import com.markz.rpccore.codec.fastjson.FastJsonEncoder;
import com.markz.rpccore.codec.frame.FrameDecoder;
import com.markz.rpccore.codec.frame.FrameEncoder;
import com.markz.rpccore.handler.RpcResponseHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * client pipeline
 */
public class RpcClientInitializer extends ChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // outbound handler
        ch.pipeline().addLast("FrameEncoder", new FrameEncoder()); // 封装成帧
        ch.pipeline().addLast("Serializer", new FastJsonEncoder()); // 序列化
        // inbound handler
        ch.pipeline().addLast("FrameDecoder", new FrameDecoder()); // 拆帧
        ch.pipeline().addLast("Deserializer", new FastJsonDecoder()); // 反序列化
        ch.pipeline().addLast("RpcResponseHandler", new RpcResponseHandler()); // 处理 RpcResponse
    }
}

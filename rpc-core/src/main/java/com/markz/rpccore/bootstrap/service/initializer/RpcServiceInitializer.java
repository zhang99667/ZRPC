package com.markz.rpccore.bootstrap.service.initializer;

import com.markz.rpccore.codec.fastjson.FastJsonDecoder;
import com.markz.rpccore.codec.fastjson.FastJsonEncoder;
import com.markz.rpccore.codec.frame.FrameDecoder;
import com.markz.rpccore.codec.frame.FrameEncoder;
import com.markz.rpccore.handler.RpcRequestHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

// 服务端 pipeline
public class RpcServiceInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        // outbound handler
        ch.pipeline().addLast("FrameEncoder", new FrameEncoder()); // 封装成帧
        ch.pipeline().addLast("Serializer", new FastJsonEncoder()); // 序列化
        // inbound handler
        ch.pipeline().addLast("FrameDecoder", new FrameDecoder()); // 拆帧
        ch.pipeline().addLast("Deserializer", new FastJsonDecoder()); // 反序列化
        ch.pipeline().addLast("RpcRequestHandler", new RpcRequestHandler()); // 处理 RpcRequest
    }
}
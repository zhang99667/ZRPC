package com.markz.rpccore.codec;

import com.markz.rpccore.config.RpcConfiguration;
import com.markz.rpccore.holder.RpcConfigurationHolder;
import com.markz.rpccore.protocol.ProtocolMessage;
import com.markz.rpccore.serializer.Serializer;
import com.markz.rpccore.serializer.SerializerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 协议体编码器（序列化）
 * 负责将 ProtocolMessage 的 body 替换为序列化后的字节数组，然后传递给一次编解码器
 */
public class RpcBodyEncoder extends MessageToMessageEncoder<ProtocolMessage<?>> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolMessage<?> msg, List<Object> out) throws Exception {
        RpcConfiguration rpcConfig = RpcConfigurationHolder.getRpcConfig();
        // 序列化消息体
        Serializer serializer = SerializerFactory.getSerializer(rpcConfig.getSerializerType());
        // 将 ProtocolMessage 的 body 替换为序列化后的字节数组，然后传递给一次编解码器
        byte[] bodyBytes = serializer.serialize(msg.getBody());
        out.add(bodyBytes);
    }
}

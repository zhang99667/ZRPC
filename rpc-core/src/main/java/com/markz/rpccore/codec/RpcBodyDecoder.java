package com.markz.rpccore.codec;

import com.markz.common.entity.rpc.RpcRequest;
import com.markz.common.entity.rpc.RpcResponse;
import com.markz.rpccore.config.RpcConfiguration;
import com.markz.rpccore.holder.RpcConfigurationHolder;
import com.markz.rpccore.protocol.ProtocolMessageTypeEnum;
import com.markz.rpccore.serializer.Serializer;
import com.markz.rpccore.serializer.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 二次解码协议体
 * 反序列化
 */
public class RpcBodyDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 1. 读取协议头信息
        byte magic = byteBuf.readByte();            // 魔数
        byte type = byteBuf.readByte();             // 消息类型 (请求/响应/心跳等)
        long requestId = byteBuf.readLong();        // 请求ID
        int bodyLength = byteBuf.readInt();         // 消息体长度

        // 2. 取出协议体
        byte[] bodyBytes = new byte[bodyLength];
        byteBuf.readBytes(bodyBytes);

        // 3. 反序列化
        RpcConfiguration rpcConfig = RpcConfigurationHolder.getRpcConfig();
        Serializer serializer = SerializerFactory.getSerializer(rpcConfig.getSerializerType());

        if (ProtocolMessageTypeEnum.getEnumByKey(type) == ProtocolMessageTypeEnum.REQUEST) {
            RpcRequest rpcRequest = serializer.deserialize(bodyBytes, RpcRequest.class);
            list.add(rpcRequest);
        } else {
            RpcResponse rpcResponse = serializer.deserialize(bodyBytes, RpcResponse.class);
            list.add(rpcResponse);
        }
    }
}

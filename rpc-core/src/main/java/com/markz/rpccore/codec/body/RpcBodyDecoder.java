package com.markz.rpccore.codec.body;

import com.markz.common.entity.rpc.RpcRequest;
import com.markz.common.entity.rpc.RpcResponse;
import com.markz.rpccore.config.RpcConfiguration;
import com.markz.rpccore.holder.RpcConfigurationHolder;
import com.markz.rpccore.protocol.ProtocolHeader;
import com.markz.rpccore.protocol.ProtocolMessage;
import com.markz.rpccore.protocol.ProtocolMessageTypeEnum;
import com.markz.rpccore.serializer.Serializer;
import com.markz.rpccore.serializer.SerializerFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 二次解码协议体
 * 反序列化
 */
public class RpcBodyDecoder extends MessageToMessageDecoder<ProtocolMessage<byte[]>> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ProtocolMessage<byte[]> protocolMessage, List<Object> list) throws Exception {
        // 1. 读取协议头信息
        ProtocolHeader header = protocolMessage.getProtocolHeader();
        byte type = header.getType();               // 消息类型 (请求/响应/心跳等)
        long requestId = header.getRequestId();     // 请求ID
        int bodyLength = header.getBodyLength();    // 消息体长度

        // 2. 取出协议体
        byte[] bodyBytes = protocolMessage.getBody();

        // 3. 反序列化
        RpcConfiguration rpcConfig = RpcConfigurationHolder.getRpcConfig();
        Serializer serializer = SerializerFactory.getSerializer(rpcConfig.getSerializerType());
        if (ProtocolMessageTypeEnum.getEnumByKey(type) == ProtocolMessageTypeEnum.REQUEST) {
            RpcRequest rpcRequest = serializer.deserialize(bodyBytes, RpcRequest.class);
            list.add(rpcRequest);
        } else if (ProtocolMessageTypeEnum.getEnumByKey(type) == ProtocolMessageTypeEnum.RESPONSE) {
            RpcResponse rpcResponse = serializer.deserialize(bodyBytes, RpcResponse.class);
            list.add(rpcResponse);
        }
    }
}

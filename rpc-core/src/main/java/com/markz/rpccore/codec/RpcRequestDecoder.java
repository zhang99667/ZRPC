package com.markz.rpccore.codec;

import com.markz.common.entity.rpc.RpcRequest;
import com.markz.rpccore.serializer.fastjson.FastJsonSerializer;
import com.markz.rpccore.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 二次解码
 * 反序列化
 */
public class RpcRequestDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Serializer serializer = new FastJsonSerializer();
        int length = byteBuf.readableBytes();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        // 反序列化
        RpcRequest rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
        list.add(rpcRequest);
    }
}

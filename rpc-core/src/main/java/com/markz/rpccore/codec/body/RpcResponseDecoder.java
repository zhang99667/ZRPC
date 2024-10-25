package com.markz.rpccore.codec.body;

import com.markz.common.entity.rpc.RpcResponse;
import com.markz.rpccore.serializer.Serializer;
import com.markz.rpccore.serializer.fastjson.FastJsonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 二次解码
 * 反序列化
 */
@Deprecated
public class RpcResponseDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Serializer serializer = new FastJsonSerializer();
        int length = byteBuf.readableBytes();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        // 反序列化
        RpcResponse rpcResponse = serializer.deserialize(bytes, RpcResponse.class);
        list.add(rpcResponse);
    }
}

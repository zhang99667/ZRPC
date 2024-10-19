package com.markz.rpccore.codec.jdk;

import com.markz.rpccore.serializer.jdk.JdkSerializer;
import com.markz.rpccore.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 二次编码
 * 序列化
 */
public class JdkEncoder extends MessageToMessageEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object object, List list) throws Exception {
        Serializer serializer = new JdkSerializer();
        byte[] bytes = serializer.serialize(object);
        ByteBuf buffer = channelHandlerContext.alloc().buffer(bytes.length);
        buffer.writeBytes(bytes);
        list.add(buffer);
    }
}

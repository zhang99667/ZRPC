package com.markz.rpccore.codec.frame;

import com.markz.rpccore.constant.ProtocolConstant;
import com.markz.rpccore.protocol.ProtocolHeader;
import com.markz.rpccore.protocol.ProtocolMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 协议头编码器
 * 负责封装协议头和消息体长度（协议体在上一个 Handler 中已经被序列化为 byte 数组）
 */
public class ProtocolEncoder extends MessageToByteEncoder<ProtocolMessage<byte[]>> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolMessage<byte[]> msg, ByteBuf out) {
        ProtocolHeader header = msg.getProtocolHeader();

        // 1. 写入协议头
        out.writeByte(ProtocolConstant.PROTOCOL_MAGIC); // 写入魔数
        out.writeByte(header.getType()); // 写入消息类型
        out.writeLong(header.getRequestId()); // 写入请求 ID

        byte[] body = msg.getBody();
        int bodyLength = body.length;
        out.writeInt(bodyLength); // 写入消息体长度

        // 2. 写入消息体
        out.writeBytes(body);
    }
}

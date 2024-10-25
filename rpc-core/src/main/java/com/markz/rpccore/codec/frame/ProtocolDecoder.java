package com.markz.rpccore.codec.frame;

import com.markz.rpccore.constant.ProtocolConstant;
import com.markz.rpccore.protocol.ProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 协议一次解码器
 */
public class ProtocolDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        /*
           判断数据包大小是否够，不够协议头大小直接返回
           1 byte 魔数 + 1 byte type + 8 byte requestId + 4 byte bodyLength
         */
        if (in.readableBytes() < ProtocolConstant.MESSAGE_HEADER_LENGTH) {
            return;
        }

        // 1. 读取协议头
        byte magic = in.readByte(); // 读取魔数
        byte type = in.readByte(); // 读取消息类型
        long requestId = in.readLong(); // 读取请求 ID
        int bodyLength = in.readInt(); // 读取消息体长度

        // 2. 构造协议头对象
        ProtocolHeader header = new ProtocolHeader(magic, type, requestId, bodyLength);

        // 3. 确保消息体内容已到达
        if (in.readableBytes() < bodyLength) {
            in.resetReaderIndex();  // 如果消息体没有到达，重置读取位置，等待更多数据
            return;
        }

        // 4. 将解码后的头信息传递到下一个处理器
        out.add(header);
    }
}

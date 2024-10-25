package com.markz.rpccore.codec.protocol;

import com.markz.rpccore.constant.ProtocolConstant;
import com.markz.rpccore.protocol.ProtocolHeader;
import com.markz.rpccore.protocol.ProtocolMessage;
import com.markz.rpccore.protocol.ProtocolMessageTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 协议一次解码器
 * <p>
 * 这里踩了一个坑，花了一天时间。
 * 原因如下：
 * <p>
 * <a href="https://blog.csdn.net/u011412234/article/details/54929360">Netty接收到一个请求但是代码段执行了两次</a>
 */
public class ProtocolDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
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

        // 2. 安全校验
        if (securityCheck(ctx, magic, type, bodyLength)) return;

        // 3. 构造协议头对象
        ProtocolHeader header = new ProtocolHeader(magic, type, requestId, bodyLength);

        // 4. 确保消息体内容已到达
        if (in.readableBytes() < bodyLength) {
            in.resetReaderIndex();  // 如果消息体没有到达，重置读取位置，等待更多数据
            return;
        }

        // 5. 将 message 传递到下一个处理器
        byte[] bodyBytes = new byte[bodyLength];
        in.readBytes(bodyBytes);

        ProtocolMessage<byte[]> protocolMessage = new ProtocolMessage<>(header, bodyBytes);
        out.add(protocolMessage);
    }

    /**
     * 安全校验
     *
     * @param ctx        ChannelHandlerContext
     * @param magic      魔数
     * @param type       消息类型
     * @param bodyLength 消息体长度
     * @return true if invalid packet detected
     */
    private static boolean securityCheck(ChannelHandlerContext ctx, byte magic, byte type, int bodyLength) {
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            ctx.close();
            throw new IllegalArgumentException("Invalid magic number: " + magic);
        }

        if (ProtocolMessageTypeEnum.getEnumByKey(type) == null) {
            ctx.close();
            throw new IllegalArgumentException("Invalid message type: " + type);
        }

        // 允许的消息体长度检查
        if (bodyLength < 0 || bodyLength > ProtocolConstant.MAX_BODY_LENGTH) {
            ctx.close();
            throw new IllegalArgumentException("Invalid body length: " + bodyLength);
        }

        return false;
    }
}
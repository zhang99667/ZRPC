package com.markz.rpccore.codec.frame;

import com.markz.rpccore.constant.ProtocolConstant;
import com.markz.rpccore.protocol.ProtocolHeader;
import com.markz.rpccore.protocol.ProtocolMessageTypeEnum;
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

        // 2. 安全校验
        if (securityCheck(ctx, in, magic, type, bodyLength)) return;

        // 3. 构造协议头对象
        ProtocolHeader header = new ProtocolHeader(magic, type, requestId, bodyLength);

        // 4. 确保消息体内容已到达
        if (in.readableBytes() < bodyLength) {
            in.resetReaderIndex();  // 如果消息体没有到达，重置读取位置，等待更多数据
            return;
        }

        // 5. 将解码后的头信息传递到下一个处理器
        out.add(header);
    }

    /**
     * 安全校验
     *
     * @param ctx        ChannelHandlerContext
     * @param in         ByteBuf
     * @param magic      魔数
     * @param type       消息类型
     * @param bodyLength 消息体长度
     * @return
     */
    private static boolean securityCheck(ChannelHandlerContext ctx, ByteBuf in, byte magic, byte type, int bodyLength) {
        // 1. 校验魔数是否正确
        if (magic != ProtocolConstant.PROTOCOL_MAGIC) {
            ctx.close();  // 关闭连接，表示非法协议包
            throw new IllegalArgumentException("Invalid magic number: " + magic);
        }

        // 2. 校验消息类型是否合法
        if (ProtocolMessageTypeEnum.getEnumByKey(type) == null) {
            ctx.close();  // 关闭连接，表示非法消息类型
            throw new IllegalArgumentException("Invalid message type: " + type);
        }

        // 3. 校验消息体长度是否合理
        if (bodyLength < 0 || bodyLength > in.readableBytes()) {
            in.resetReaderIndex();  // 重置读指针，等待更多数据
            return true;
        }
        return false;
    }
}

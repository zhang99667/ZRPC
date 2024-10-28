package com.markz.rpccore.constant;

/**
 * 一次解码器 Frame 常量（传输协议）
 * 两端要商量好一致
 */
public class ProtocolConstant {

    /**
     * 消息头长度 Byte
     */
    public static final Integer MESSAGE_HEADER_LENGTH = 0xE;

    /**
     * 最大消息体长度
     * <p>
     * MAX_BODY_LENGTH = MAX_FRAME_LENGTH - LENGTH_FIELD_OFFSET
     */
    public static final int MAX_BODY_LENGTH = 0x3F6;

    /**
     * 魔数
     */
    public static final byte PROTOCOL_MAGIC = 0x1;


    // ************* FrameDecoder *************
    /**
     * 最大帧长度 1kb
     */
    public static final Integer MAX_FRAME_LENGTH = 0x400;

    /**
     * 长度字段的偏移量
     */
    public static final Integer LENGTH_FIELD_OFFSET = 0xA;

    /**
     * 长度字段的长度
     */
    public static final Integer LENGTH_FIELD_LENGTH = 0x4;

    /**
     * 长度字段的调整值
     */
    public static final Integer LENGTH_ADJUSTMENT = 0x0;

    /**
     * 解码后丢弃的长度
     */
    public static final Integer INITIAL_BYTES_TO_STRIP = 0x0;
}

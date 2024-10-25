package com.markz.rpccore.constant;

/**
 * 一次解码器 Frame 常量（传输协议）
 * 两端要商量好一致
 */
public class ProtocolConstant {

    /**
     * 消息头长度 Byte
     */
    public static final Integer MESSAGE_HEADER_LENGTH = 14;

    /**
     * 魔数
     */
    public static final byte PROTOCOL_MAGIC = 0x1;


    /**
     * 最大帧长度
     */
    public static final Integer MAX_FRAME_LENGTH = Integer.MAX_VALUE;

    /**
     * 长度字段的偏移量
     */
    public static final Integer LENGTH_FIELD_OFFSET = 0;

    /**
     * 长度字段的长度
     */
    public static final Integer LENGTH_FIELD_LENGTH = 4;

    /**
     * 长度字段的调整值
     */
    public static final Integer LENGTH_ADJUSTMENT = 0;

    /**
     * 跳过长度字段后的初始读取位置
     */
    public static final Integer INITIAL_BYTES_TO_STRIP = 4;

}

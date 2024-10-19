package com.markz.rpccore.constant;

/**
 * 一次解码器 Frame 常量
 * 两端要商量好一致
 */
public class ProtocolConstant {

    /**
     * 最大帧长度
     */
    public static final Integer MAX_FRAME_LENGTH = Integer.MAX_VALUE;

    public static final Integer LENGTH_FIELD_OFFSET = 0;

    public static final Integer LENGTH_FIELD_LENGTH = 4;

    public static final Integer LENGTH_ADJUSTMENT = 0;

    // 跳过长度字段后的初始读取位置
    public static final Integer INITIAL_BYTES_TO_STRIP = 4;
}

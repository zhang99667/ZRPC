package com.markz.rpccore.constant;

/**
 * 心跳机制常量
 */
public class HeartBeatConstant {

    /**
     * 最大空闲次数
     */
    public static final int MAX_IDLE_COUNT = 3;

    /**
     * 读空闲
     */
    public static final int READER_IDLE_TIME_SECONDS = 10;

    /**
     * 写空闲
     */
    public static final int WRITER_IDLE_TIME_SECONDS = 5;

    /**
     * 两者空闲
     */
    public static final int ALL_IDLE_TIME_SECONDS = 0;
}

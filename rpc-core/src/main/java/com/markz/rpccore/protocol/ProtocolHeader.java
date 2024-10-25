package com.markz.rpccore.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 协议头结构
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolHeader {

    /**
     * 魔数，保证安全
     */
    private byte magic;

    /**
     * 消息类型（请求，响应，心跳）
     */
    private byte type;

    /**
     * 请求 id，8byte
     */
    private long requestId;

    /**
     * 消息体长度，4byte
     */
    private int bodyLength;
}

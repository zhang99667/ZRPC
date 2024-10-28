package com.markz.rpccore.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 协议消息结构
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolMessage<T> {

    /**
     * 协议头
     */
    protected ProtocolHeader protocolHeader;

    /**
     * 协议体
     */
    protected T body;
}

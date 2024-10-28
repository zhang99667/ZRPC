package com.markz.rpccore.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 心跳消息
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HeartBeatMessage<T> extends ProtocolMessage<T> {
}
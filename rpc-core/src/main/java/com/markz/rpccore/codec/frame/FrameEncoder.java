package com.markz.rpccore.codec.frame;

import com.markz.rpccore.constant.ProtocolConstant;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 一次编码
 * 解决半包粘包
 */
public class FrameEncoder extends LengthFieldPrepender {
    public FrameEncoder() {
        super(ProtocolConstant.LENGTH_FIELD_LENGTH);
    }
}
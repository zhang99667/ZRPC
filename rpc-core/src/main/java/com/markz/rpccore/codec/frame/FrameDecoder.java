package com.markz.rpccore.codec.frame;

import com.markz.rpccore.constant.ProtocolConstant;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 一次解码
 * 解决半包粘包
 */
public class FrameDecoder extends LengthFieldBasedFrameDecoder {
    public FrameDecoder() {
        super(ProtocolConstant.MAX_FRAME_LENGTH,
                ProtocolConstant.LENGTH_FIELD_OFFSET,
                ProtocolConstant.LENGTH_FIELD_LENGTH,
                ProtocolConstant.LENGTH_ADJUSTMENT,
                ProtocolConstant.INITIAL_BYTES_TO_STRIP);
    }
}
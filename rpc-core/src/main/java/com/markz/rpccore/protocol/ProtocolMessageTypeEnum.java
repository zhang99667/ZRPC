package com.markz.rpccore.protocol;

/**
 * 协议消息类型枚举，请求、响应、心跳
 */
public enum ProtocolMessageTypeEnum {

    REQUEST(0),

    RESPONSE(1),

    HEART_BEAT(2);

    private final int key;

    ProtocolMessageTypeEnum(int key) {
        this.key = key;
    }

    /**
     * 根据 key 值获取枚举类
     *
     * @param key key
     * @return 枚举类
     */
    public static ProtocolMessageTypeEnum getEnumByKey(int key) {
        for (ProtocolMessageTypeEnum anEnum : ProtocolMessageTypeEnum.values()) {
            if (anEnum.key == key) {
                return anEnum;
            }
        }
        return null;
    }
}

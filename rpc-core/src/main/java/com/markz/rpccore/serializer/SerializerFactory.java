package com.markz.rpccore.serializer;

import com.markz.rpccore.serializer.fastjson.FastJsonSerializer;
import com.markz.rpccore.util.SpiLoader;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new FastJsonSerializer();

    /**
     * 获取序列化器实例
     *
     * @param key key
     * @return instance
     */
    public static Serializer getSerializer(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }
}

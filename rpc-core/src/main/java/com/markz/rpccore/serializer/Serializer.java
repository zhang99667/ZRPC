package com.markz.rpccore.serializer;

import java.io.IOException;

/**
 * 序列化接口
 */
public interface Serializer {
    /**
     * 序列化
     *
     * @param object 序列化的对象
     * @param <T>    对象的泛型
     * @return 二进制字节流
     */
    <T> byte[] serialize(T object) throws IOException;

    /**
     * 反序列化
     *
     * @param bytes 二进制字节流
     * @param clazz  反序列化类型
     * @param <T>   类型的泛型
     * @return 类
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException;
}
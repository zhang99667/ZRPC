package com.markz.rpccore.serializer.fastjson;

import com.alibaba.fastjson.JSON;
import com.markz.rpccore.serializer.Serializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FastJsonSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        return JSON.toJSONString(object).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        return JSON.parseObject(bytes, clazz);
    }
}

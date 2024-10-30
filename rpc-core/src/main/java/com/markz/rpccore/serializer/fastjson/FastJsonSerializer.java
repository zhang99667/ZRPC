package com.markz.rpccore.serializer.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.markz.rpccore.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FastJsonSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T object) {
        return JSON.toJSONString(object).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        // 将 byte 数组转换为 InputStream，以便 JSONReader 读取
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        JSONReader reader = JSONReader.of(inputStream, StandardCharsets.UTF_8);
        // 设置所需特性
        reader.getContext().config(JSONReader.Feature.SupportClassForName);
        return reader.read(clazz);
    }
}

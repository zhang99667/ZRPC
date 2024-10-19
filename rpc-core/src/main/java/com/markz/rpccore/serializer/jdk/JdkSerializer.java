package com.markz.rpccore.serializer.jdk;

import com.markz.rpccore.serializer.Serializer;

import java.io.*;

/**
 * JDK 序列化器
 */
public class JdkSerializer implements Serializer {
    /**
     * 序列化
     *
     * @param object 对象
     * @param <T>    泛型
     * @return 字节流
     * @throws IOException IOException
     */
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param bytes 字节流
     * @param clazz  对象类型
     * @param <T>   泛型
     * @return 对象
     * @throws IOException IOException
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            return (T) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

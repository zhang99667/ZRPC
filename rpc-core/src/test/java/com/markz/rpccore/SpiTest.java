package com.markz.rpccore;

import com.markz.rpccore.serializer.Serializer;
import com.markz.rpccore.util.SpiLoader;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;

@SpringBootTest
public class SpiTest {
    @Test
    void test() {
        SpiLoader.loadAll();
        Serializer instance = SpiLoader.getInstance(Serializer.class, "fastjson");
        try {
            byte[] serialize = instance.serialize("hello world");
            System.out.println("serialize = " + Arrays.toString(serialize));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

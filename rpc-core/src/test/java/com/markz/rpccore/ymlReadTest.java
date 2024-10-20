package com.markz.rpccore;

import com.markz.rpccore.config.RpcConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ymlReadTest {
    @Resource
    private RpcConfig rpcConfig;

    @Test
    void test() {
        System.out.println("rpcConfig.getName() = " + rpcConfig.getName());
        System.out.println("rpcConfig.getVersion() = " + rpcConfig.getVersion());
    }
}
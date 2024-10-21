package com.markz.rpccore;

import com.markz.rpccore.config.RpcConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ymlReadTest {
    @Resource
    private RpcConfiguration rpcConfiguration;

    @Test
    void test() {
        System.out.println("rpcConfig.getName() = " + rpcConfiguration.getName());
        System.out.println("rpcConfig.getVersion() = " + rpcConfiguration.getVersion());
    }
}
package com.markz.provider;

import com.markz.rpccore.bootstrap.service.NettyRpcService;
import com.markz.rpccore.bootstrap.service.RpcServiceBootstrap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.Resource;

@SpringBootApplication
@ComponentScan(value = "com.markz")
public class ProviderApplication implements CommandLineRunner {

    @Resource
    private RpcServiceBootstrap rpcServiceBootstrap;

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        rpcServiceBootstrap.run();
    }
}

package com.markz.rpccore;

import com.markz.rpccore.config.RegistryConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
public class ZkClientTest {

    private CuratorFramework zkClient;

    @Resource
    private RegistryConfiguration registryConfiguration;

    public final String ZK_ROOT = "/rpc";

    /**
     * 初始化 zk 客户端
     */
    @Test
    void start() throws InterruptedException {
        init();
        registerService("com.markz.Service.UserService", "localhost:8081");
        Thread.sleep(1000);
        String s = discoverService("com.markz.Service.UserService");
        System.out.println(s);
        Thread.sleep(1000);
        shutdown();
    }

    /**
     * 初始化 zk 客户端
     */
    public void init() {
        String zkAddress = registryConfiguration.getZkAddress();
        int zkTimeout = registryConfiguration.getTimeout();
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(zkAddress)
                .connectionTimeoutMs(zkTimeout)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        zkClient.start();
        log.info("Zookeeper 客户端已启动，连接地址: {}", zkAddress);
    }

    /**
     * 关闭 zk 客户端
     */
    public void shutdown() {
        if (zkClient != null) {
            zkClient.close();
            log.info("Zookeeper 客户端已关闭！");
        }
    }

    /**
     * 注册服务到Zookeeper
     *
     * @param serviceName    服务名称
     * @param serviceAddress 服务地址
     */
    public void registerService(String serviceName, String serviceAddress) {
        try {
            String path = ZK_ROOT + "/" + serviceName + "/" + serviceAddress;
            zkClient.create().creatingParentsIfNeeded().forPath(path);
            log.info("服务注册成功: {}", path);
        } catch (Exception e) {
            log.error("服务注册失败: {}", e.getMessage());
        }
    }

    /**
     * 从Zookeeper发现服务
     *
     * @param serviceName 服务名称
     * @return 服务地址
     */
    public String discoverService(String serviceName) {
        try {
            String path = ZK_ROOT + "/" + serviceName;
            // 获取注册的服务地址列表
            List<String> serviceAddresses = zkClient.getChildren().forPath(path);
            log.info("发现服务，数量: {}", serviceAddresses.size());
            // 简单返回第一个服务地址，可以根据实际情况实现负载均衡
            return serviceAddresses.isEmpty() ? null : serviceAddresses.get(0);
        } catch (Exception e) {
            log.error("服务发现失败: {}", e.getMessage());
            return null;
        }
    }
}

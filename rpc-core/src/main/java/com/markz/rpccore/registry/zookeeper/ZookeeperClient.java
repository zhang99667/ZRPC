package com.markz.rpccore.registry.zookeeper;

import com.markz.rpccore.config.RegistryConfiguration;
import com.markz.rpccore.model.ServiceProviderMeta;
import com.markz.rpccore.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Zookeeper 客户端，负责连接 Zookeeper 并进行服务注册和发现
 */
@Slf4j
// @Component
public class ZookeeperClient implements ServiceRegistry {

    public CuratorFramework zkClient;

    @Resource
    private RegistryConfiguration registryConfiguration;

    /**
     * 初始化 zk 客户端
     */
    @Override
    public void init() {
        String zkAddress = registryConfiguration.getZkAddress();
        int zkTimeout = registryConfiguration.getTimeout();
        // 1. 构建 client 实例
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(zkAddress)
                .connectionTimeoutMs(zkTimeout)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        // TODO 服务发现要用 ServiceDiscoveryBuilder
        // 2. 构建 serviceDiscovery 实例
        // serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMetaInfo.class)
        //         .client(client)
        //         .basePath(ZK_ROOT_PATH)
        //         .serializer(new JsonInstanceSerializer<>(ServiceMetaInfo.class))
        //         .build();


        // 3. 启动 zkClient 和 serviceDiscovery
        try {
            zkClient.start();
            log.info("Zookeeper 客户端已启动，连接地址: {}", zkAddress);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 注册服务到 Zookeeper
     *
     * @param serviceProviderMeta 服务提供者元信息
     */
    @Override
    public void registerService(ServiceProviderMeta serviceProviderMeta) {
        String zkRoot = registryConfiguration.getZkRoot();
        String serviceKey = serviceProviderMeta.getServiceKey();

        try {
            String path = zkRoot + "/" + serviceKey;
            zkClient.create().creatingParentsIfNeeded().forPath(path);
            log.info("服务注册成功: {}", path);
        } catch (Exception e) {
            log.error("服务注册失败: {}", e.getMessage());
        }
    }

    /**
     * 服务下线
     *
     * @param serviceProviderMeta 服务提供者元信息
     */
    @Override
    public void unRegisterService(ServiceProviderMeta serviceProviderMeta) {
        String zkRoot = registryConfiguration.getZkRoot();
        String serviceKey = serviceProviderMeta.getServiceKey();

        try {
            String path = zkRoot + "/" + serviceKey;
            zkClient.delete().deletingChildrenIfNeeded().forPath(path);
            log.info("服务下线成功：{}", path);
        } catch (Exception e) {
            log.info("服务下线失败：{}", e.getMessage());
        }

    }

    /**
     * 服务发现
     *
     * @param serviceName Service 类全限定名
     * @return List<ServiceProviderMeta>
     */
    @Override
    public List<ServiceProviderMeta> serviceDiscovery(String serviceName) {
        try {
            String path = registryConfiguration.getZkRoot() + "/" + serviceName;
            // 获取注册的服务地址列表
            List<String> serviceAddresses = zkClient.getChildren().forPath(path);
            List<ServiceProviderMeta> serviceProviderMetaList = new ArrayList<>();

            // 将服务地址字符串转换为 ServiceProviderMeta 对象
            for (String serviceAddress : serviceAddresses) {
                String[] hostPort = serviceAddress.split(":");
                if (hostPort.length == 2) {
                    ServiceProviderMeta serviceProviderMeta = new ServiceProviderMeta();
                    serviceProviderMeta.setServiceName(serviceName);  // 设置服务名称
                    serviceProviderMeta.setServiceHost(hostPort[0]); // 设置主机名
                    serviceProviderMeta.setServicePort(hostPort[1]); // 设置端口号
                    serviceProviderMeta.setWeight(100); // 默认权重，可从配置中读取
                    serviceProviderMetaList.add(serviceProviderMeta);
                }
            }

            log.info("发现服务，数量: {}", serviceAddresses.size());
            return serviceProviderMetaList;

        } catch (Exception e) {
            log.error("服务发现失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 关闭 zk 客户端
     */
    @Override
    public void shutdown() {
        if (zkClient != null) {
            zkClient.close();
            log.info("Zookeeper 客户端已关闭！");
        }
    }
}

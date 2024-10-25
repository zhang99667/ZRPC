package com.markz.rpccore.request;


import com.markz.common.entity.rpc.RpcRequest;
import com.markz.common.entity.rpc.RpcResponse;
import com.markz.rpccore.bootstrap.client.NettyRpcClient;
import com.markz.rpccore.config.RegistryConfiguration;
import com.markz.rpccore.config.RpcConfiguration;
import com.markz.rpccore.holder.RpcConfigurationHolder;
import com.markz.rpccore.loadbalancer.LoadBalance;
import com.markz.rpccore.loadbalancer.LoadBalanceFactory;
import com.markz.rpccore.model.ServiceProviderMeta;
import com.markz.rpccore.protocol.ProtocolHeader;
import com.markz.rpccore.protocol.ProtocolMessage;
import com.markz.rpccore.protocol.ProtocolMessageTypeEnum;
import com.markz.rpccore.registry.Registry;
import com.markz.rpccore.registry.RegistryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 封装 RpcRequest 请求
 */
@Slf4j
@Component
public class RpcRequestManager {

    @Resource
    private NettyRpcClient nettyRpcClient;

    public RpcResponse sendRequest(RpcRequest rpcRequest) {
        if (rpcRequest == null) {
            // TODO 自定义异常
            throw new RuntimeException("请求不能为空");
        }

        // 0. 读取配置文件
        RegistryConfiguration registryConfig = RpcConfigurationHolder.getRegistryConfig();
        RpcConfiguration rpcConfig = RpcConfigurationHolder.getRpcConfig();

        // 1. 注册中心拿到服务列表
        Registry registry = RegistryFactory.getRegistry(registryConfig.getType());
        List<ServiceProviderMeta> serviceProviderMetaList = registry.serviceDiscovery(rpcRequest.getServiceName());

        if (!serviceProviderMetaList.isEmpty()) {
            // 2. load balance 选择出服务提供者
            // TODO 暂时还不知道怎么把节点权重加进去
            LoadBalance loadBalancer = LoadBalanceFactory.getLoadBalancer(rpcConfig.getLoadbalancerType());
            ServiceProviderMeta serviceProviderMeta = loadBalancer.selectServer(serviceProviderMetaList);

            // 3. 封装协议
            ProtocolMessage<RpcRequest> message = new ProtocolMessage<>();
            ProtocolHeader protocolHeader = ProtocolHeader.builder()
                    .requestId(rpcRequest.getRequestId())
                    .type((byte) ProtocolMessageTypeEnum.REQUEST.ordinal())
                    .build();
            message.setProtocolHeader(protocolHeader);
            message.setBody(rpcRequest);

            // 4. 发送数据
            return getResponse(serviceProviderMeta, message);
        } else {
            log.error("{} 服务当前不可用！", rpcRequest.getServiceName());
            return RpcResponse.builder()
                    .exception(new RuntimeException(rpcRequest.getServiceName() + "服务不可用"))
                    .build();
        }
    }

    /**
     * 发送 RpcRequest 拿到 RpcResponse
     *
     * @param serviceProviderMeta 服务提供者元信息
     * @param message             ProtocolMessage<RpcRequest>
     * @return RpcResponse
     */
    private RpcResponse getResponse(ServiceProviderMeta serviceProviderMeta, ProtocolMessage<RpcRequest> message) {
        String serviceHost = serviceProviderMeta.getServiceHost();
        int servicePort = serviceProviderMeta.getServicePort();
        return nettyRpcClient.connectionAndSend(serviceHost, servicePort, message);
    }
}
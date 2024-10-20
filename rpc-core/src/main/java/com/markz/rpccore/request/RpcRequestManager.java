package com.markz.rpccore.request;


import com.markz.common.entity.rpc.RpcRequest;
import com.markz.common.entity.rpc.RpcResponse;
import com.markz.rpccore.bootstrap.client.NettyRpcClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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

        // TODO 1. 检查注册中心缓存是否有这个方法的提供者
        // TODO 2. load balance
        // 3. 发送数据包
        return getResponse(rpcRequest);
    }

    private RpcResponse getResponse(RpcRequest rpcRequest) {
        // TODO 这里暂时写死，后面注册中心填上
        String host = "localhost";
        int port = 8090;
        return nettyRpcClient.connectionAndSend(host, port, rpcRequest);
    }
}
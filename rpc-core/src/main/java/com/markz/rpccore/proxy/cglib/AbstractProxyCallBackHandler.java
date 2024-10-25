package com.markz.rpccore.proxy.cglib;

import com.markz.common.entity.rpc.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 动态代理回调抽象类，方便做 mock
 */
@Slf4j
public abstract class AbstractProxyCallBackHandler implements MethodInterceptor {

    /**
     * 封装 RpcRequest
     *
     * @param method 被动态代理的方法
     * @return RpcRequest
     */
    public RpcRequest getRpcRequest(Method method) {
        String methodName = method.getName();
        String serviceName = method.getDeclaringClass().getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Parameter[] args = method.getParameters();

        log.info("代理调用拦截, method={}.{}", serviceName, methodName);
        // 1. 封装 RpcRequest
        RpcRequest rpcRequest = new RpcRequest();
        // TODO 需要一个 id 生成器
        rpcRequest.setRequestId(1L);
        rpcRequest.setMethodName(methodName);
        rpcRequest.setServiceName(serviceName);
        rpcRequest.setArgs(args);
        rpcRequest.setParameterTypes(parameterTypes);
        return rpcRequest;
    }

    /**
     * 发送 RpcRequest，拿到 RpcResponse
     *
     * @param rpcRequest RpcRequest
     * @return RpcResponse
     */
    public abstract <T> T getRpcResponse(RpcRequest rpcRequest);
}

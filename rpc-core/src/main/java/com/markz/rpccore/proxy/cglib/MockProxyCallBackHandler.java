package com.markz.rpccore.proxy.cglib;

import com.markz.common.entity.rpc.RpcRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Mock 反射回调
 */
@Slf4j
@Component
public class MockProxyCallBackHandler extends AbstractProxyCallBackHandler {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
        RpcRequest rpcRequest = getRpcRequest(method, objects);
        return getRpcResponse(rpcRequest);
    }

    @Override
    public <T> T getRpcResponse(RpcRequest rpcRequest) {
        String serviceName = rpcRequest.getServiceName();
        String methodName = rpcRequest.getMethodName();
        return (T) (serviceName + "." + methodName + "hello");
    }
}

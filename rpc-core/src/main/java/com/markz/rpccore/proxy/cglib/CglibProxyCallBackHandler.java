package com.markz.rpccore.proxy.cglib;

import com.markz.common.entity.rpc.RpcRequest;
import com.markz.common.entity.rpc.RpcResponse;
import com.markz.rpccore.request.RpcRequestManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;


/**
 * Cglib 动态代理回调
 */
@Slf4j
@Component
public class CglibProxyCallBackHandler extends AbstractProxyCallBackHandler {

    @Resource
    private RpcRequestManager rpcRequestManager;

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        // 过滤 Object 类中的方法，避免拦截
        if (method.getDeclaringClass() == Object.class) {
            return methodProxy.invokeSuper(o, objects);
        }
        // 1. 封装 RpcRequest
        RpcRequest rpcRequest = getRpcRequest(method);
        // 2. 发送 RpcRequest，拿到 RpcResponse
        return getRpcResponse(rpcRequest);
    }

    @Override
    public RpcResponse getRpcResponse(RpcRequest rpcRequest) {
        // 2. 发送 RpcRequest
        if (rpcRequestManager == null) {
            // TODO 需要封装自定义异常类
            throw new RuntimeException("Spring IOC Exception");
        }
        RpcResponse rpcResponse = rpcRequestManager.sendRequest(rpcRequest);
        if (rpcResponse.getException() != null) {
            log.error("远程调用过程出现异常, {}", rpcResponse.getException().getMessage());
            throw new RuntimeException(rpcResponse.getException());
        }
        return rpcResponse;
    }
}
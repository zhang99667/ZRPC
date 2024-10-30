package com.markz.rpccore.proxy.cglib;

import com.alibaba.fastjson2.JSONObject;
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
        // 过滤 Object 类中的方法，Object 是基类，调用它的方法都会被拦截
        if (method.getDeclaringClass() == Object.class) {
            return methodProxy.invokeSuper(o, objects);
        }
        // 1. 封装 RpcRequest
        RpcRequest rpcRequest = getRpcRequest(method, objects);

        // 2. 发送 RpcRequest，拿到 RpcResponse
        RpcResponse rpcResponse = getRpcResponse(rpcRequest);
        if (rpcResponse.getException() != null) {
            rpcResponse.setResult("当前服务不可用");
            // TODO 封装自定义异常
            throw new RuntimeException(rpcResponse.getException());
        }
        // 3. 将拿到的 result 反序列化为 Object
        Object result = rpcResponse.getResult();
        Class<?> resultType = rpcResponse.getResultType();
        if (result instanceof JSONObject) {
            result = ((JSONObject) result).toJavaObject(resultType);
        }
        return result;
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
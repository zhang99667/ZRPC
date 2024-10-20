package com.markz.rpccore.handler;

import com.markz.common.entity.rpc.RpcRequest;
import com.markz.common.entity.rpc.RpcResponse;
import com.markz.rpccore.util.SpringBeanFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 接受 Request
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
        log.info("RpcRequestHandler 收到请求为:{}", rpcRequest);

        // 1. 解析请求
        String requestId = rpcRequest.getRequestId();
        String serviceName = rpcRequest.getServiceName();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();

        // 2. 创建响应
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(requestId);

        // 3. 寻找目标 bean，通过反射调用
        try {
            Object bean = SpringBeanFactory.getBean(Class.forName(serviceName));
            Method method = bean.getClass().getMethod(methodName, parameterTypes);
            Object result = method.invoke(bean, args);
            // 4. 封装响应
            rpcResponse.setResult(result);

        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            rpcResponse.setException(e);
            log.info("{}方法执行失败: {}", methodName, e.getMessage());
        } finally {
            // 5. 返回结果
            ctx.channel().writeAndFlush(rpcRequest);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接就绪");
        super.channelActive(ctx);
    }
}
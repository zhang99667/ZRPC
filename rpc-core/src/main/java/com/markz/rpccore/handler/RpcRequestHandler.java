package com.markz.rpccore.handler;

import com.markz.common.entity.rpc.RpcRequest;
import com.markz.common.entity.rpc.RpcResponse;
import com.markz.rpccore.protocol.ProtocolHeader;
import com.markz.rpccore.protocol.ProtocolMessage;
import com.markz.rpccore.protocol.ProtocolMessageTypeEnum;
import com.markz.rpccore.util.SpringBeanFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 接受 Request，返回 Response
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) {
        log.info("收到请求:{}", rpcRequest);

        // 1. 解析请求
        Long requestId = rpcRequest.getRequestId();
        String serviceName = rpcRequest.getServiceName();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] args = rpcRequest.getArgs();

        // 2. 创建响应
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(requestId);

        // 3. 寻找目标 bean，反射回调结果
        try {
            Object bean = SpringBeanFactory.getBean(Class.forName(serviceName));
            Method method = bean.getClass().getMethod(methodName, parameterTypes);
            Object result = method.invoke(bean, args);
            // 4. 封装响应
            rpcResponse.setResult(result);

        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            // 4. 封装响应
            rpcResponse.setException(e);
            log.info("{}方法执行失败: {}", methodName, e.getMessage());
        } finally {
            // 5. 返回结果
            ProtocolMessage<Object> message = new ProtocolMessage<>();
            ProtocolHeader header = ProtocolHeader.builder()
                    .requestId(rpcResponse.getRequestId())
                    .type((byte) ProtocolMessageTypeEnum.RESPONSE.ordinal())
                    .build();
            message.setProtocolHeader(header);
            message.setBody(rpcResponse);
            ctx.channel().writeAndFlush(message).addListener(future -> {
                if (future.isSuccess()) {
                    log.info("RpcResponse 发送成功: {}", message);
                } else {
                    log.error("RpcResponse 发送失败: {}", future.cause().getMessage());
                    throw new RuntimeException("RpcResponse 发送失败: {}" + future.cause().getMessage());
                }
            });
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接就绪");
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("通道异常：{}", cause.getMessage());
        ctx.close();
    }
}
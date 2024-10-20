package com.markz.rpccore.request;

import com.markz.common.entity.rpc.RpcResponse;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;

/**
 * 用来拿到 Netty nio 线程异步执行结果
 */
public class RequestPromise extends DefaultPromise<RpcResponse> {
    public RequestPromise(EventExecutor executor) {
        super(executor);
    }
}

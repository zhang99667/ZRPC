package com.markz.rpccore.proxy.cglib;


import com.markz.rpccore.config.RpcConfiguration;
import com.markz.rpccore.holder.RpcConfigurationHolder;
import com.markz.rpccore.proxy.ProxyFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Service 代理工厂，通过 CGLIB 生成
 */
@Component
public class CglibRequestProxyFactory implements ProxyFactory {

    @Resource
    private CglibProxyCallBackHandler cglibProxyCallBackHandler;

    @Resource
    private MockProxyCallBackHandler mockProxyCallBackHandler;

    @Override
    public <T> T getProxyInstance(Class<T> clazz) {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        // TODO 这里 RpcConfigurationHolder 还没初始化，报了空指针异常。
        // RpcConfiguration rpcConfig = RpcConfigurationHolder.getRpcConfig();
        // if (rpcConfig.isMock()) {
        //     enhancer.setCallback(mockProxyCallBackHandler);
        // } else {
        enhancer.setCallback(cglibProxyCallBackHandler);
        // }
        return (T) enhancer.create();
    }
}
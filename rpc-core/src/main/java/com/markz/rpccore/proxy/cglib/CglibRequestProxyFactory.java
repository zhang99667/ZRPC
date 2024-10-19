package com.markz.rpccore.proxy.cglib;


import com.markz.rpccore.proxy.ProxyFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Service 代理工厂，通过 CGLIB 生成
 */
@Component
public class CglibRequestProxyFactory implements ProxyFactory {

    // TODO 这里要能根据配置文件自动切换

    @Resource
    private CglibProxyCallBackHandler cglibProxyCallBackHandler;

    @Resource
    private MockProxyCallBackHandler mockProxyCallBackHandler;

    @Override
    public <T> T getProxyInstance(Class<T> clazz) {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(cglibProxyCallBackHandler);
        return (T) enhancer.create();
    }
}
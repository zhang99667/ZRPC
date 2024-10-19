package com.markz.rpccore.proxy;

/**
 * 代理工厂类
 */
public interface ProxyFactory {
    <T> T getProxyInstance(Class<T> clazz);
}

package com.markz.rpccore.spring;

import com.markz.rpccore.annotation.RpcReference;
import com.markz.rpccore.proxy.cglib.CglibRequestProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;

@Slf4j
@Component
public class RpcAnnotationProcessor implements BeanPostProcessor {

    @Resource
    private CglibRequestProxyFactory requestProxyFactory;

    /**
     * 1. 扫描客户端注解注入 Service 代理对象
     * 2. 扫描服务端注解，注册服务
     *
     * @param bean     原始 bean
     * @param beanName 原始 beanName
     * @return 增强后的 bean
     * @throws BeansException BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            injectServiceProxy(bean, declaredField);
        }
        return bean;
    }

    /**
     * 客户端 Service 代理注入
     *
     * @param bean          原始 bean
     * @param declaredField bean 声明的字段
     */
    private void injectServiceProxy(Object bean, Field declaredField) {
        RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
        if (rpcReference != null) {
            if (!declaredField.isAccessible()) {
                declaredField.setAccessible(true);
            }
            // 1. 通过代理工厂拿到动态代理
            Class<?> aClass = declaredField.getType();
            Object proxyInstance = requestProxyFactory.getProxyInstance(aClass);
            if (proxyInstance != null) {
                // 2. 替换原来的 bean 为代理对象
                try {
                    declaredField.set(bean, proxyInstance);
                } catch (IllegalAccessException e) {
                    log.info("代理对象[{}]注入失败", declaredField.getName());
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

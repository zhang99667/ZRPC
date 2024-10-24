package com.markz.rpccore.spring;

import com.markz.rpccore.annotation.RpcService;
import com.markz.rpccore.holder.RpcConfigurationHolder;
import com.markz.rpccore.model.ServiceProviderMeta;
import com.markz.rpccore.registry.Registry;
import com.markz.rpccore.registry.RegistryFactory;
import com.markz.rpccore.util.NetworkUtils;
import com.markz.rpccore.util.SpringBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 扫描 RpcService，注册服务
 */
@Component
public class RegisterRpcServices {
    /**
     * 扫描 RpcService 注解
     */
    public void registerServices() {
        // 获取 Spring 上下文中的所有 Bean
        String[] beanNames = SpringBeanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object bean = SpringBeanFactory.getBean(beanName);
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            if (rpcService != null) {
                registerService(bean);
            }
        }
    }

    /**
     * 在注册中心注册有 @RpcService 的 Service
     *
     * @param bean bean
     */
    private void registerService(Object bean) {
        Class<?> serviceInterface = bean.getClass().getAnnotation(RpcService.class).serviceInterface();
        String serviceName = serviceInterface.getName();
        String serviceHost = null;
        int servicePort = RpcConfigurationHolder.getRpcConfig().getServerPort();
        serviceHost = NetworkUtils.getLocalHostIp();

        ServiceProviderMeta serviceProviderMeta = new ServiceProviderMeta();
        serviceProviderMeta.setServiceName(serviceName);
        serviceProviderMeta.setServiceHost(serviceHost);
        serviceProviderMeta.setServicePort(servicePort);
        // 注册服务到 Zookeeper
        String type = RpcConfigurationHolder.getRegistryConfig().getType();
        Registry registry = RegistryFactory.getRegistry(type);
        registry.registerService(serviceProviderMeta);
    }
}

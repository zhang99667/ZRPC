package com.markz.rpccore.spring;

import com.markz.rpccore.annotation.RpcService;
import com.markz.rpccore.model.ServiceProviderMeta;
import com.markz.rpccore.registry.ServiceRegistry;
import com.markz.rpccore.registry.zookeeper.ZookeeperClient;
import com.markz.rpccore.util.SpringBeanFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 扫描 RpcService，注册服务
 */
@Component
public class RegisterRpcServices {

    // @Resource
    // private ZookeeperClient registry;

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
        String serviceName = bean.getClass().getName();
        String serviceHost = "localhost";
        String servicePort = "8090";

        ServiceProviderMeta serviceProviderMeta = new ServiceProviderMeta();
        serviceProviderMeta.setServiceName(serviceName);
        serviceProviderMeta.setServiceHost(serviceHost);
        serviceProviderMeta.setServicePort(servicePort);
        // 注册服务到 Zookeeper
        // registry.registerService(serviceProviderMeta);
    }
}

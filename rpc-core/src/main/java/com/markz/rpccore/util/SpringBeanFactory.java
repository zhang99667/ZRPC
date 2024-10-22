package com.markz.rpccore.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * ApplicationContextAware 工具类，用来从 ioc 容器中拿 bean
 */
@Component
public class SpringBeanFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 设置 applicationContext
     *
     * @param applicationContext ApplicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringBeanFactory.applicationContext == null) {
            SpringBeanFactory.applicationContext = applicationContext;
        }
    }

    /**
     * 获取 ApplicationContext
     */
    private static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过名称获取 Bean
     *
     * @param beanName beanName
     * @return bean
     */
    public static Object getBean(String beanName) {
        return getApplicationContext().getBean(beanName);
    }

    /**
     * 通过类型获取 Bean
     *
     * @param clazz service class
     * @param <T>   T
     * @return bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过名称和类型获取 Bean
     *
     * @param name  beanName
     * @param clazz service class
     * @param <T>   T
     * @return bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 获取 Spring 上下文中的所有 Bean
     *
     * @return BeanDefinitionNames
     */
    public static String[] getBeanDefinitionNames() {
        return getApplicationContext().getBeanDefinitionNames();
    }
}

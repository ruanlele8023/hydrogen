package com.example.hydrogen.config.client.interfaces;

/**
 * 创建代理接口类
 */
public interface ProxyCreator {
    Object createProxy(Class<?> type);
}

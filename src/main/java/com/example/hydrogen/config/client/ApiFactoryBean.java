package com.example.hydrogen.config.client;

import com.example.hydrogen.client.IDemoApi;
import com.example.hydrogen.config.client.interfaces.ProxyCreator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiFactoryBean {
    @Bean
    ProxyCreator jdkProxyCreate() { return new JdkProxyCreator(); }

    @Bean
    FactoryBean<IDemoApi> demApi(ProxyCreator proxyCreator) {
        return new FactoryBean<IDemoApi>() {
            @Override
            public IDemoApi getObject() throws Exception {
                return (IDemoApi) proxyCreator.createProxy(this.getObjectType());
            }

            @Override
            public Class<?> getObjectType() {
                return IDemoApi.class;
            }
        };
    }
}

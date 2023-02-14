package com.example.hydrogen.config.client;

import com.example.hydrogen.annotation.ApiServer;
import com.example.hydrogen.config.client.interfaces.ProxyCreator;
import com.example.hydrogen.data.bean.MethodInfoBean;
import com.example.hydrogen.data.bean.ServerInfoBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class JdkProxyCreator implements ProxyCreator, EnvironmentAware {

    private JdkProxyCreator() {};

    public  JdkProxyCreator(Environment environment) {
        this.environment = environment;
    }

    private Environment environment;

    @Override
    public Object createProxy(Class<?> type) {
        log.info("create proxy: [{}]", type);

        var handler = new WebClientRestHandler();

        var serverInfo = loadServerInfo(type);

        log.info("serverInfo: url[{}]", serverInfo.getUrl());

        handler.init(serverInfo);

        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{type}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                var methodInfo = loadMethodInfo(method, args);
                log.info("api methodInfo: [{}]", methodInfo);
                return handler.invokeRest(serverInfo, methodInfo);
            }
        });
    }

    private MethodInfoBean loadMethodInfo(Method method, Object[] args) {
        var methodInfo = new MethodInfoBean();
        // TODO
        return methodInfo;
    }

    private ServerInfoBean loadServerInfo(Class<?> type) {
        var serverInfo = new ServerInfoBean();
        var annotation = type.getAnnotation(ApiServer.class);

        var url = environment.resolvePlaceholders(annotation.value());
        serverInfo.setUrl(url);

        return serverInfo;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

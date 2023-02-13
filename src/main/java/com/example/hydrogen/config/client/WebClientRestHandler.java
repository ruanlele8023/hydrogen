package com.example.hydrogen.config.client;

import com.example.hydrogen.config.client.interfaces.RestHandler;
import com.example.hydrogen.data.bean.MethodInfoBean;
import com.example.hydrogen.data.bean.ServerInfoBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class WebClientRestHandler implements RestHandler {

    private WebClient client;

    @Override
    public void init(ServerInfoBean serverInfo) {

    }

    @Override
    public Object invokeRest(ServerInfoBean serverInfo, MethodInfoBean methodInfo) {
        return null;
    }
}

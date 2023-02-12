package com.example.hydrogen.config.webFilter;

import com.example.hydrogen.util.Constants;
import com.example.hydrogen.util.IdGeneratorUtil;
import com.example.hydrogen.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Configuration
public class LogRequestAndTracedIdFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange).contextWrite(context -> {

            var requestId = Optional.of(exchange.getRequest())
                    .map(HttpMessage::getHeaders)
                    .map(it -> it.getFirst(Constants.REQUEST_ID))
                    .filter(StringUtils::hasLength)
                    .orElse(IdGeneratorUtil.generateRequestId.get());

            MDC.put(Constants.REQUEST_ID, requestId);

            var contextTemp = context.put(Constants.REQUEST_ID, requestId);

            context.put(Constants.REQUEST_ID, requestId);
            exchange.getAttributes().put(Constants.REQUEST_ID, requestId);

            var serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
            var serverHttpRequest = serverRequest.exchange().getRequest();
            // 不能对 body 内容进行 block() 后的消费打印
            log.info("http request path: [{}], queryParams:[{}]", serverHttpRequest.getPath(), JsonUtils.toJSONString(serverHttpRequest.getQueryParams()));

            return contextTemp;
        });
    }
}

package com.example.hydrogen.config.webFilter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;

@Slf4j
@Configuration
public class LogResponseFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest httpRequest = exchange.getRequest();
        final var httpUrl = httpRequest.getURI().toString();

        ServerHttpResponseDecorator loggingServerHttpResponseDecorator = new ServerHttpResponseDecorator(exchange.getResponse()) {
            String responseBody = "";

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                Mono<DataBuffer> buffer = Mono.from(body);
                return super.writeWith(buffer.doOnNext(dataBuffer -> {
                    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                        Channels.newChannel(byteArrayOutputStream).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
                        responseBody = IOUtils.toString(byteArrayOutputStream.toByteArray(), "UTF-8");
                        log.info("[http response] {} body {}", httpUrl, responseBody);
                    } catch (Exception e) {
                        log.error("[http response] {} body {}", httpUrl, responseBody, e);
                    }
                }));
            }
        };
        return chain.filter(exchange.mutate().response(loggingServerHttpResponseDecorator).build());
    }
}
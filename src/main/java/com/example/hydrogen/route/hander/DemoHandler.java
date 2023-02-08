package com.example.hydrogen.route.hander;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class DemoHandler {
    public Mono<ServerResponse> getStatus(ServerRequest request) {
        return ServerResponse.ok().body(Mono.just("success"), String.class);
    }
}

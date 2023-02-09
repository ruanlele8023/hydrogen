package com.example.hydrogen.route.hander;

import com.example.hydrogen.config.MonoExt;
import com.example.hydrogen.data.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class DemoHandler {
    public Mono<ServerResponse> getStatus(ServerRequest request) {
        return ServerResponse.ok().body(MonoExt.wrapWithErrorLog(Mono.just("id"), "abcd"), Result.class);
    }
}

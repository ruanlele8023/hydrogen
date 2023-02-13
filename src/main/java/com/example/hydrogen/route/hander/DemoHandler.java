package com.example.hydrogen.route.hander;

import com.example.hydrogen.config.MonoExt;
import com.example.hydrogen.data.Result;
import com.example.hydrogen.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DemoHandler {

    private final DemoService demoService;
    public Mono<ServerResponse> getStatus(ServerRequest request) {
        var id = request.pathVariable("id");
        return ServerResponse.ok().body(MonoExt.wrapWithErrorLog(demoService.demo(id), "abcd"), Result.class);
    }
}

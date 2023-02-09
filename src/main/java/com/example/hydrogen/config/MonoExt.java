package com.example.hydrogen.config;

import com.example.hydrogen.data.Result;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
public final class MonoExt {

    private MonoExt() {
    }

    public static <T> Mono<Result<T>> wrapWithErrorLog(Mono<T> mono, String requestId) {
        return mono.map(data -> Result.of(requestId, data))
                .onErrorResume(t -> {
                    log.error("<{}>, failed: ", requestId, t);
                    return Mono.just(Result.failure(requestId, "error", t.getMessage()));
                })
                .defaultIfEmpty(Result.of(requestId, null));
    }
}

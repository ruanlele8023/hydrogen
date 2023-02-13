package com.example.hydrogen.client;

import com.example.hydrogen.annotation.ApiServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@ApiServer(value = "a")
public interface IDemoApi {
    @GetMapping("/demo")
    Mono<String> demo(@PathVariable("id") String id);
}

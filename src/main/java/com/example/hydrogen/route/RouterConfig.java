package com.example.hydrogen.route;

import com.example.hydrogen.route.hander.DemoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> routes(DemoHandler demoHandler) {
        return route()
                .GET("/demo/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), demoHandler::getStatus)
                .build();
    }
}

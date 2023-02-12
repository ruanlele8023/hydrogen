package com.example.hydrogen.config;

import com.example.hydrogen.util.ApplicationContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(ApplicationContextHolder.getBean(ObjectMapper.class)));
        configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(ApplicationContextHolder.getBean(ObjectMapper.class)));
    }
}

package com.example.hydrogen.service;

import com.example.hydrogen.client.IDemoApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DemoService {
    private final IDemoApi iDemoApi;

    public Mono<?> demo(final String id) {
        return iDemoApi.demo(id);
    }
}

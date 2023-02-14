package com.example.hydrogen.annotation;

import com.example.hydrogen.config.client.ApiServerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ApiServerRegistrar.class)
public @interface EnableApiServer {
}

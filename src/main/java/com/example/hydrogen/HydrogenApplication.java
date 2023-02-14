package com.example.hydrogen;

import com.example.hydrogen.annotation.EnableApiServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@EnableApiServer
@SpringBootApplication
public class HydrogenApplication {

    public static void main(String[] args) {
        SpringApplication.run(HydrogenApplication.class, args);
    }

}

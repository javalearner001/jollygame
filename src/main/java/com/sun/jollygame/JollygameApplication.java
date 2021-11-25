package com.sun.jollygame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JollygameApplication {

    public static void main(String[] args) {
        SpringApplication.run(JollygameApplication.class, args);
    }

}

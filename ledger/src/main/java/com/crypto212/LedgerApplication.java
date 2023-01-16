package com.crypto212;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class LedgerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LedgerApplication.class, args);
    }

}
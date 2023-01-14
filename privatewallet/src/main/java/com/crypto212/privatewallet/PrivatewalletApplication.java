package com.crypto212.privatewallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class PrivatewalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrivatewalletApplication.class, args);
    }

}

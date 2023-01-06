package com.crypto212.apigw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients(
        basePackages = "com.crypto212.clients"
)
@PropertySource("classpath:clients-${spring.profiles.active}.properties")
public class ApiGWApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGWApplication.class, args);
    }
}

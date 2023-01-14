package com.crypto212.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
//@EnableFeignClients(
//        basePackages = "com.crypto212.clients"
//)
//@PropertySources({
//        @PropertySource("classpath:clients-${spring.profiles.active}.properties")
//})
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}

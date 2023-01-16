package com.crypto212.config;

import com.crypto212.idgenerator.SnowFlake;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {
    @Value("${snowflake.workerId}")
    private Long workerId;
//    @Bean
//    public GlobalExceptionHandler globalExceptionHandler(){
//        return new GlobalExceptionHandler();
//    }

    @Bean
    public SnowFlake snowFlake(){
        return new SnowFlake(workerId);
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}

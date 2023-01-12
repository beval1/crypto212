package com.crypto212.wallet.config;

import com.crypto212.idgenerator.SnowFlake;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {
    @Value("${snowflake.workerId}")
    private long snowFlakeWorkerId;

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public SnowFlake snowFlake() {
        return new SnowFlake(snowFlakeWorkerId);
    }
}

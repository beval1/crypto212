package com.crypto212.payment.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${app.stripe.secretKey}")
    private String stripeSecretKey;
    @Bean
    public String stripe(){
        Stripe.apiKey = stripeSecretKey;
        return stripeSecretKey;
    }
}

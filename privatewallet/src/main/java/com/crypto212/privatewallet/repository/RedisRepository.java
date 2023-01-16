package com.crypto212.privatewallet.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepository {
    private final RedisTemplate<String, BigDecimal> redisTemplate;

    public RedisRepository(RedisTemplate<String, BigDecimal> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void insertData(String assetSymbol, BigDecimal data) {
        redisTemplate.opsForValue().set(assetSymbol, data);
        redisTemplate.expire(assetSymbol, 12, TimeUnit.HOURS);
    }

    public BigDecimal getData(String assetSymbol) {
        return redisTemplate.opsForValue().get(assetSymbol);
    }
}

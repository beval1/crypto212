package com.crypto212.marketengine.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvgPrice {
    private double price;
    private String mins;

    public AvgPrice(String data){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            AvgPrice avgPrice = objectMapper.readValue(data, AvgPrice.class);
            this.price = avgPrice.getPrice();
            this.mins = avgPrice.getMins();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
